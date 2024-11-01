package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.book.domain.Book;
import page.clab.api.domain.library.bookLoanRecord.application.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.ApproveBookLoanUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.external.library.book.application.port.ExternalRegisterBookUseCase;
import page.clab.api.external.library.book.application.port.ExternalRetrieveBookUseCase;

@Service
@RequiredArgsConstructor
public class BookLoanApprovalService implements ApproveBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ExternalRetrieveBookUseCase externalRetrieveBookUseCase;
    private final ExternalRegisterBookUseCase externalRegisterBookUseCase;

    /**
     * 도서 대출을 승인합니다.
     *
     * <p>대출 기록을 조회하고, 도서와 대출자의 상태를 검증합니다.
     * 도서가 이미 대출 중인지 확인하고, 대출자의 대출 한도를 검증합니다.
     * 승인된 대출 기록과 도서 정보를 저장한 후 대출 기록 ID를 반환합니다.</p>
     *
     * @param bookLoanRecordId 대출 기록의 ID
     * @return 승인된 대출 기록의 ID
     */
    @Transactional
    @Override
    public Long approveBookLoan(Long bookLoanRecordId) {
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.getById(bookLoanRecordId);
        String borrowerId = bookLoanRecord.getBorrowerId();
        Book book = externalRetrieveBookUseCase.getById(bookLoanRecord.getBookId());

        book.validateBookIsNotBorrowed();
        validateBorrowLimit(borrowerId);
        bookLoanRecord.approve();
        book.borrowBook(borrowerId);
        externalRegisterBookUseCase.save(book);
        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = externalRetrieveBookUseCase.countByBorrowerId(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }
}