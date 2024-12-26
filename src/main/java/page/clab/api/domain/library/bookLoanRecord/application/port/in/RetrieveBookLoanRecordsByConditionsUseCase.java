package page.clab.api.domain.library.bookLoanRecord.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBookLoanRecordsByConditionsUseCase {

    PagedResponseDto<BookLoanRecordResponseDto> retrieveBookLoanRecords(Long bookId, String borrowerId,
        BookLoanStatus status, Pageable pageable);
}
