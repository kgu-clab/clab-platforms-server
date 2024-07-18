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
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class BookLoanApprovalService implements ApproveBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ExternalRetrieveBookUseCase externalRetrieveBookUseCase;
    private final ExternalRegisterBookUseCase externalRegisterBookUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long approveBookLoan(Long bookLoanRecordId) {
        String borrowerId = externalRetrieveMemberUseCase.getCurrentMemberId();
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByIdOrThrow(bookLoanRecordId);
        Book book = externalRetrieveBookUseCase.findByIdOrThrow(bookLoanRecord.getBookId());

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
