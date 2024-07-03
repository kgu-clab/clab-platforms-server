package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.RetrieveBookDetailsUseCase;
import page.clab.api.domain.book.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookDetailsRetrievalService implements RetrieveBookDetailsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBookPort retrieveBookPort;
    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;

    @Transactional(readOnly = true)
    @Override
    public BookDetailsResponseDto retrieve(Long bookId) {
        MemberBasicInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberBasicInfo();
        Book book = retrieveBookPort.findByIdOrThrow(bookId);
        return mapToBookDetailsResponseDto(book, currentMemberInfo.getMemberName());
    }

    @NotNull
    private BookDetailsResponseDto mapToBookDetailsResponseDto(Book book, String borrowerName) {
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookDetailsResponseDto.toDto(book, borrowerName, dueDate);
    }

    private LocalDateTime getDueDateForBook(Book book) {
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByBookAndReturnedAtIsNullAndStatus(book, BookLoanStatus.APPROVED)
                .orElse(null);
        return bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
    }
}
