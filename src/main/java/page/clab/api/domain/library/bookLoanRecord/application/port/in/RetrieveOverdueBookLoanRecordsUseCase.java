package page.clab.api.domain.library.bookLoanRecord.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveOverdueBookLoanRecordsUseCase {

    PagedResponseDto<BookLoanRecordOverdueResponseDto> retrieveOverdueBookLoanRecords(Pageable pageable);
}
