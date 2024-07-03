package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.ApproveBookLoanUseCase;
import page.clab.api.domain.book.application.port.out.CountBooksByBorrowerPort;
import page.clab.api.domain.book.application.port.out.LoadBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.LoadBookPort;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanApprovalService implements ApproveBookLoanUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final LoadBookPort loadBookPort;
    private final LoadBookLoanRecordPort loadBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final CountBooksByBorrowerPort countBooksByBorrowerPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long approve(Long bookLoanRecordId) {
        String borrowerId = retrieveMemberUseCase.getCurrentMemberId();
        BookLoanRecord bookLoanRecord = loadBookLoanRecordPort.findByIdOrThrow(bookLoanRecordId);
        Book book = loadBookPort.findByIdOrThrow(bookLoanRecord.getBook().getId());

        book.validateBookIsNotBorrowed();
        validateBorrowLimit(borrowerId);
        bookLoanRecord.approve();

        validationService.checkValid(bookLoanRecord);
        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = countBooksByBorrowerPort.countByBorrowerId(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }
}
