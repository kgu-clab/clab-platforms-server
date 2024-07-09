package page.clab.api.domain.book.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.book.domain.BookLoanStatus;

public interface BookLoanRecordRepositoryCustom {
    Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable);

    Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable);
}
