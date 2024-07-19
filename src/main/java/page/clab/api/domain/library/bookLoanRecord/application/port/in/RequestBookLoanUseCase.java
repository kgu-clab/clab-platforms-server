package page.clab.api.domain.library.bookLoanRecord.application.port.in;

import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

public interface RequestBookLoanUseCase {
    Long requestBookLoan(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException;
}
