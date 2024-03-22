package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BookLoanRecordService {

    private final BookService bookService;

    private final MemberService memberService;

    private final BookRepository bookRepository;

    private final NotificationService notificationService;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    public Long borrowBook(BookLoanRecordRequestDto requestDto) throws CustomOptimisticLockingFailureException {
        try {
            Member borrower = memberService.getCurrentMember();
            borrower.checkLoanSuspension();

            Book book = bookService.getBookByIdOrThrow(requestDto.getBookId());
            book.borrowTo(borrower);
            bookRepository.save(book);

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book, borrower);
            notificationService.sendNotificationToMember(borrower.getId(), "[" + book.getTitle() + "] 도서 대출이 완료되었습니다.");
            return bookLoanRecordRepository.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출에 실패했습니다. 다시 시도해주세요.");
        }
    }

    @Transactional
    public Long returnBook(BookLoanRecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Book book = bookService.getBookByIdOrThrow(requestDto.getBookId());
        book.returnBook(currentMember);
        bookRepository.save(book);

        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        bookLoanRecord.markAsReturned();

        notificationService.sendNotificationToMember(currentMember.getId(), "[" + book.getTitle() + "] 도서 반납이 완료되었습니다.");
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    @Transactional
    public Long extendBookLoan(BookLoanRecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Book book = bookService.getBookByIdOrThrow(requestDto.getBookId());

        book.validateCurrentBorrower(currentMember);
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        bookLoanRecord.extendLoan();

        notificationService.sendNotificationToMember(currentMember.getId(), "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.");
        return bookLoanRecordRepository.save(bookLoanRecord).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BookLoanRecordResponseDto> getBookLoanRecordsByConditions(Long bookId, String borrowerId, Boolean isReturned, Pageable pageable) {
        Page<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordRepository.findByConditions(bookId, borrowerId, isReturned, pageable);
        return new PagedResponseDto<>(bookLoanRecords);
    }

    public BookLoanRecord getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(Book book) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNull(book)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

}