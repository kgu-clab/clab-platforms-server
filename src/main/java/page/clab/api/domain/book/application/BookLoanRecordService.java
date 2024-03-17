package page.clab.api.domain.book.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.book.exception.InvalidBorrowerException;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.book.exception.OverdueException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookLoanRecordService {

    private final BookService bookService;

    private final MemberService memberService;

    private final BookRepository bookRepository;

    private final NotificationService notificationService;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    public Long borrowBook(BookLoanRecordRequestDto dto) throws CustomOptimisticLockingFailureException {
        try {
            Member borrower = memberService.getCurrentMember();
            borrower.checkLoanSuspension();

            Book book = bookService.getBookByIdOrThrow(dto.getBookId());
            book.borrowTo(borrower);
            bookRepository.save(book);

            BookLoanRecord bookLoanRecord = BookLoanRecord.create(book, borrower);
            notificationService.sendNotificationToMember(
                    borrower.getId(),
                    "[" + book.getTitle() + "] 도서 대출이 완료되었습니다."
            );
            return bookLoanRecordRepository.save(bookLoanRecord).getId();
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출에 실패했습니다. 다시 시도해주세요.");
        }
    }

    @Transactional
    public Long returnBook(BookLoanRecordRequestDto dto) {
        Member borrower = memberService.getCurrentMember();
        Book book = bookService.getBookByIdOrThrow(dto.getBookId());
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrower.getId())) {
            throw new InvalidBorrowerException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime extensionDate = bookLoanRecord.getDueDate();
        if (currentDate.isAfter(extensionDate)) {
            handleOverdueAndSuspension(borrower, ChronoUnit.DAYS.between(extensionDate, currentDate));
        }
        book.setBorrower(null);
        bookRepository.save(book);
        bookLoanRecord.setReturnedAt(currentDate);
        Long id = bookLoanRecordRepository.save(bookLoanRecord).getId();
        notificationService.sendNotificationToMember(
                borrower.getId(),
                "[" + book.getTitle() + "] 도서 반납이 완료되었습니다."
        );
        return id;
    }

    @Transactional
    public Long extendBookLoan(BookLoanRecordRequestDto bookLoanRecordRequestDto) {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = memberService.getCurrentMember().getId();
        Book book = bookService.getBookByIdOrThrow(bookId);
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId)) {
            throw new InvalidBorrowerException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
        Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(book);
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime extensionDate = bookLoanRecord.getDueDate();
        Long loanExtensionCount = bookLoanRecord.getLoanExtensionCount();
        if (borrower.getLoanSuspensionDate() != null && currentDate.isBefore(borrower.getLoanSuspensionDate())) {
            throw new LoanSuspensionException("대출 정지 중입니다. 연장할 수 없습니다.");
        }
        if (currentDate.isAfter(extensionDate)) {
            throw new LoanSuspensionException("연체 중인 도서는 연장할 수 없습니다.");
        }
        if (loanExtensionCount >= 2) {
            throw new OverdueException("대출 연장 횟수를 초과했습니다.");
        }
        bookLoanRecord.setDueDate(extensionDate.plusWeeks(2));
        bookLoanRecord.setLoanExtensionCount(loanExtensionCount + 1);
        Long id = bookLoanRecordRepository.save(bookLoanRecord).getId();

        notificationService.sendNotificationToMember(
                borrowerId,
                "[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다."
        );
        return id;
    }

    public PagedResponseDto<BookLoanRecordResponseDto> getBookLoanRecordsByConditions(Long bookId, String borrowerId, Boolean isReturned, Pageable pageable) {
        Page<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordRepository.findByConditions(bookId, borrowerId, isReturned, pageable);
        return new PagedResponseDto<>(bookLoanRecords);
    }

    private void handleOverdueAndSuspension(Member member, long overdueDays) {
        LocalDateTime suspensionEndDate = LocalDateTime.now().plusDays(overdueDays * 7);
        member.setLoanSuspensionDate(suspensionEndDate);
        memberService.saveMember(member);
    }

    public BookLoanRecord getBookLoanRecordByBookAndReturnedAtIsNullOrThrow(Book book) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNull(book)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

}