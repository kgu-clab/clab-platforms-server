package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.BooksByConditionsRetrievalUseCase;
import page.clab.api.domain.book.application.port.out.LoadBookLoanRecordByBookAndStatusPort;
import page.clab.api.domain.book.application.port.out.RetrieveBooksByConditionsPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BooksByConditionsRetrievalService implements BooksByConditionsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveBooksByConditionsPort retrieveBooksByConditionsPort;
    private final LoadBookLoanRecordByBookAndStatusPort loadBookLoanRecordByBookAndStatusPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookResponseDto> retrieve(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        Page<Book> books = retrieveBooksByConditionsPort.findByConditions(title, category, publisher, borrowerId, borrowerName, pageable);
        return new PagedResponseDto<>(books.map(this::mapToBookResponseDto));
    }

    @NotNull
    private BookResponseDto mapToBookResponseDto(Book book) {
        MemberBasicInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberBasicInfo();
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookResponseDto.toDto(book, currentMemberInfo.getMemberName(), dueDate);
    }

    private LocalDateTime getDueDateForBook(Book book) {
        BookLoanRecord bookLoanRecord = loadBookLoanRecordByBookAndStatusPort.findByBookAndReturnedAtIsNullAndStatus(book, BookLoanStatus.APPROVED)
                .orElse(null);
        return bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
    }
}
