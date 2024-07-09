package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.application.exception.BookAlreadyAppliedForLoanException;
import page.clab.api.domain.book.application.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.book.application.port.in.RequestBookLoanUseCase;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanRequestService implements RequestBookLoanUseCase {

    private final RetrieveBookPort retrieveBookPort;
    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final SendNotificationUseCase notificationService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long requestBookLoan(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException {
        try {
            MemberBorrowerInfoDto borrowerInfo = retrieveMemberInfoUseCase.getCurrentMemberBorrowerInfo();

            borrowerInfo.checkLoanSuspension();
            validateBorrowLimit(borrowerInfo.getMemberId());

            Book book = retrieveBookPort.findByIdOrThrow(requestDto.getBookId());
            checkIfLoanAlreadyApplied(book, borrowerInfo.getMemberId());

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book, borrowerInfo);
            validationService.checkValid(bookLoanRecord);

            notificationService.sendNotificationToMember(borrowerInfo.getMemberId(), "[" + book.getTitle() + "] 도서 대출 신청이 완료되었습니다.");
            return registerBookLoanRecordPort.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출 신청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = retrieveBookPort.countByBorrowerId(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }

    private void checkIfLoanAlreadyApplied(Book book, String borrowerId) {
        retrieveBookLoanRecordPort.findByBookAndBorrowerIdAndStatus(book, borrowerId, BookLoanStatus.PENDING)
                .ifPresent(bookLoanRecord -> {
                    throw new BookAlreadyAppliedForLoanException("이미 대출 신청한 도서입니다.");
                });
    }
}
