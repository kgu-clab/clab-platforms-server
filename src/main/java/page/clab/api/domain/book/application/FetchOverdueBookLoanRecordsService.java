package page.clab.api.domain.book.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchOverdueBookLoanRecordsService {
    PagedResponseDto<BookLoanRecordOverdueResponseDto> execute(Pageable pageable);
}
