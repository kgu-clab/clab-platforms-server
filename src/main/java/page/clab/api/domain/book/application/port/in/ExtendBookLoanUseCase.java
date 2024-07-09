package page.clab.api.domain.book.application.port.in;

import page.clab.api.domain.book.application.dto.request.BookLoanRecordRequestDto;

public interface ExtendBookLoanUseCase {
    Long extendBookLoan(BookLoanRecordRequestDto requestDto);
}
