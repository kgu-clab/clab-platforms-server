package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.RetrieveBooksByConditionsUseCase;
import page.clab.api.domain.book.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BooksByConditionsRetrievalService implements RetrieveBooksByConditionsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBookPort retrieveBookPort;
    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookResponseDto> retrieve(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        Page<Book> books = retrieveBookPort.findByConditions(title, category, publisher, borrowerId, borrowerName, pageable);
        return new PagedResponseDto<>(books.map(this::mapToBookResponseDto));
    }

    @NotNull
    private BookResponseDto mapToBookResponseDto(Book book) {
        MemberBasicInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberBasicInfo();
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookResponseDto.toDto(book, currentMemberInfo.getMemberName(), dueDate);
    }

    private LocalDateTime getDueDateForBook(Book book) {
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByBookAndReturnedAtIsNullAndStatus(book, BookLoanStatus.APPROVED)
                .orElse(null);
        return bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
    }
}
