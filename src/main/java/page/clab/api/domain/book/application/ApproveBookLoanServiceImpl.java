package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ApproveBookLoanServiceImpl implements ApproveBookLoanService {

    private final MemberLookupService memberLookupService;
    private final BookRepository bookRepository;
    private final ValidationService validationService;
    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    @Override
    public Long execute(Long bookLoanRecordId) {
        String borrowerId = memberLookupService.getCurrentMemberId();
        BookLoanRecord bookLoanRecord = getBookLoanRecordByIdOrThrow(bookLoanRecordId);
        Book book = getBookByIdOrThrow(bookLoanRecord.getBook().getId());

        book.validateBookIsNotBorrowed();
        validateBorrowLimit(borrowerId);
        bookLoanRecord.approve();

        validationService.checkValid(bookLoanRecord);
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    private Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }

    private BookLoanRecord getBookLoanRecordByIdOrThrow(Long bookLoanRecordId) {
        return bookLoanRecordRepository.findById(bookLoanRecordId)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

    private void validateBorrowLimit(String borrowerId) {
        int borrowedBookCount = getNumberOfBooksBorrowedByMember(borrowerId);
        int maxBorrowableBookCount = 3;
        if (borrowedBookCount >= maxBorrowableBookCount) {
            throw new MaxBorrowLimitExceededException("대출 가능한 도서의 수를 초과했습니다.");
        }
    }

    public int getNumberOfBooksBorrowedByMember(String member) {
        return bookRepository.countByBorrowerId(member);
    }

}