package page.clab.api.domain.book.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;

public interface BookLoanRecordRepositoryCustom {

    Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, Boolean isReturned, Pageable pageable);

    Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable);

}