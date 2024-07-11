package page.clab.api.domain.library.book.application.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.application.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.library.book.application.port.in.RetrieveBookDetailsUseCase;
import page.clab.api.domain.library.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookDetailsRetrievalService implements RetrieveBookDetailsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBookPort retrieveBookPort;
    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;

    @Transactional(readOnly = true)
    @Override
    public BookDetailsResponseDto retrieveBookDetails(Long bookId) {
        MemberBasicInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberBasicInfo();
        Book book = retrieveBookPort.findByIdOrThrow(bookId);
        return mapToBookDetailsResponseDto(book, currentMemberInfo.getMemberName());
    }

    @NotNull
    private BookDetailsResponseDto mapToBookDetailsResponseDto(Book book, String borrowerName) {
        LocalDateTime dueDate = getDueDateForBook(book.getId());
        return BookDetailsResponseDto.toDto(book, borrowerName, dueDate);
    }

    private LocalDateTime getDueDateForBook(Long bookId) {
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByBookIdAndReturnedAtIsNullAndStatus(bookId, BookLoanStatus.APPROVED)
                .orElse(null);
        return bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
    }
}
