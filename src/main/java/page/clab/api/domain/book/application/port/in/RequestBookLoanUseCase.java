package page.clab.api.domain.book.application.port.in;

import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

public interface RequestBookLoanUseCase {
    Long request(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException;
}
