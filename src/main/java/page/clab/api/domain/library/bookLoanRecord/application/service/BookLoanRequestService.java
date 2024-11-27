package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.library.bookLoanRecord.application.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.library.bookLoanRecord.application.exception.BookAlreadyAppliedForLoanException;
import page.clab.api.domain.library.bookLoanRecord.application.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.RequestBookLoanUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.notificationSetting.application.dto.notification.BookLoanRecordNotificationInfo;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.ExecutivesAlertType;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;

@Service
@RequiredArgsConstructor
public class BookLoanRequestService implements RequestBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ExternalRetrieveBookUseCase externalRetrieveBookUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 도서 대출 신청을 처리합니다.
     *
     * <p>현재 로그인한 멤버의 대출 상태와 한도를 검증한 후,
     * 도서의 대출 신청이 이미 존재하는지 확인합니다. 대출 신청이 성공적으로 완료되면 멤버와 Slack에 알림을 전송하고, 대출 기록을 저장한 후 그 ID를 반환합니다.</p>
     *
     * @param requestDto 도서 대출 신청 요청 정보 DTO
     * @return 저장된 대출 기록의 ID
     * @throws CustomOptimisticLockingFailureException 동시에 다른 사용자가 대출을 신청하여 충돌이 발생한 경우 예외 발생
     * @throws MaxBorrowLimitExceededException         대출 한도를 초과한 경우 예외 발생
     * @throws BookAlreadyAppliedForLoanException      이미 신청된 도서일 경우 예외 발생
     */
    @Transactional
    @Override
    public Long requestBookLoan(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException {
        try {
            MemberBorrowerInfoDto borrowerInfo = externalRetrieveMemberUseCase.getCurrentMemberBorrowerInfo();

            borrowerInfo.checkLoanSuspension();
            validateBorrowLimit(borrowerInfo.getMemberId());

            Book book = externalRetrieveBookUseCase.getById(requestDto.getBookId());
            checkIfLoanAlreadyApplied(book.getId(), borrowerInfo.getMemberId());

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book.getId(), borrowerInfo);

            externalSendNotificationUseCase.sendNotificationToMember(borrowerInfo.getMemberId(),
                    "[" + book.getTitle() + "] 도서 대출 신청이 완료되었습니다.");

            BookLoanRecordNotificationInfo bookLoanRecordInfo = BookLoanRecordNotificationInfo.create(book,
                    borrowerInfo);
            eventPublisher.publishEvent(new NotificationEvent(this, ExecutivesAlertType.NEW_BOOK_LOAN_REQUEST, null,
                    bookLoanRecordInfo));

            return registerBookLoanRecordPort.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출 신청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = externalRetrieveBookUseCase.countByBorrowerId(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }

    private void checkIfLoanAlreadyApplied(Long bookId, String borrowerId) {
        retrieveBookLoanRecordPort.findByBookIdAndBorrowerIdAndStatus(bookId, borrowerId, BookLoanStatus.PENDING)
                .ifPresent(bookLoanRecord -> {
                    throw new BookAlreadyAppliedForLoanException("이미 대출 신청한 도서입니다.");
                });
    }
}
