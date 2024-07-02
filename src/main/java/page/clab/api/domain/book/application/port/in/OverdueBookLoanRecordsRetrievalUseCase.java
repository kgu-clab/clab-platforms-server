package page.clab.api.domain.book.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface OverdueBookLoanRecordsRetrievalUseCase {
    PagedResponseDto<BookLoanRecordOverdueResponseDto> retrieve(Pageable pageable);
}
