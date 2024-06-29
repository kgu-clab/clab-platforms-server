package page.clab.api.domain.book.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchBookLoanRecordsByConditionsService {
    PagedResponseDto<BookLoanRecordResponseDto> execute(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable);
}