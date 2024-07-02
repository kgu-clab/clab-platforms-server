package page.clab.api.domain.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;

public interface RetrieveOverdueBookLoanRecordsPort {
    Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable);
}
