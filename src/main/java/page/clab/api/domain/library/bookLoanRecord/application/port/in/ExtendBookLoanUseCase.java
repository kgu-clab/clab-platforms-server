package page.clab.api.domain.library.bookLoanRecord.application.port.in;

import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;

public interface ExtendBookLoanUseCase {
    Long extendBookLoan(BookLoanRecordRequestDto requestDto);
}
