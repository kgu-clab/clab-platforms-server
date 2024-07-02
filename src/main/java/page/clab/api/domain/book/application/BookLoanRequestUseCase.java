package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

public interface BookLoanRequestUseCase {
    Long request(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException;
}