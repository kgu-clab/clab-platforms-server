package page.clab.api.domain.book.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.RequestBookLoanService;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.exception.BookAlreadyAppliedForLoanException;
import page.clab.api.domain.book.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class RequestBookLoanServiceImpl implements RequestBookLoanService {

    private final BookRepository bookRepository;
    private final BookLoanRecordRepository bookLoanRecordRepository;
    private final MemberLookupService memberLookupService;
    private final NotificationSenderService notificationService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long execute(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException {
        try {
            MemberBorrowerInfoDto borrowerInfo = memberLookupService.getCurrentMemberBorrowerInfo();

            borrowerInfo.checkLoanSuspension();
            validateBorrowLimit(borrowerInfo.getMemberId());

            Book book = getBookByIdOrThrow(requestDto.getBookId());
            checkIfLoanAlreadyApplied(book, borrowerInfo.getMemberId());

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book, borrowerInfo);
            validationService.checkValid(bookLoanRecord);

            notificationService.sendNotificationToMember(borrowerInfo.getMemberId(), "[" + book.getTitle() + "] 도서 대출 신청이 완료되었습니다.");
            return bookLoanRecordRepository.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출 신청에 실패했습니다. 다시 시도해주세요.");
        }
    }

    private Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = bookRepository.countByBorrowerId(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }

    private void checkIfLoanAlreadyApplied(Book book, String borrowerId) {
        bookLoanRecordRepository.findByBookAndBorrowerIdAndStatus(book, borrowerId, BookLoanStatus.PENDING)
                .ifPresent(bookLoanRecord -> {
                    throw new BookAlreadyAppliedForLoanException("이미 대출 신청한 도서입니다.");
                });
    }
}
