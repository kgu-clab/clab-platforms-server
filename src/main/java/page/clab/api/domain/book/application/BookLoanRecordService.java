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
import page.clab.api.domain.book.exception.BookAlreadyBorrowedException;
import page.clab.api.domain.book.exception.InvalidBorrowerException;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.book.exception.OverdueException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.SearchResultNotExistException;

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
    public Long borrowBook(BookLoanRecordRequestDto bookLoanRecordRequestDto) throws CustomOptimisticLockingFailureException {
        try {
            Long bookId = bookLoanRecordRequestDto.getBookId();
            String borrowerId = memberService.getCurrentMember().getId();
            Book book = bookService.getBookByIdOrThrow(bookId);
            if (book.getBorrower() != null) {
                throw new BookAlreadyBorrowedException("이미 대출 중인 도서입니다.");
            }
            Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
            if (borrower.getLoanSuspensionDate() != null && LocalDateTime.now().isBefore(borrower.getLoanSuspensionDate())) {
                throw new LoanSuspensionException("대출 정지 중입니다. 대출 정지일까지는 책을 대출할 수 없습니다.");
            }
            book.setBorrower(borrower);
            bookRepository.save(book);
            BookLoanRecord bookLoanRecord = BookLoanRecord.builder()
                    .book(book)
                    .borrower(borrower)
                    .borrowedAt(LocalDateTime.now())
                    .dueDate(LocalDateTime.now().plusWeeks(1))
                    .loanExtensionCount(0L)
                    .build();
            Long id = bookLoanRecordRepository.save(bookLoanRecord).getId();
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(borrowerId)
                    .content("[" + book.getTitle() + "] 도서 대출이 완료되었습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDto);
            return id;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomOptimisticLockingFailureException("도서 대출에 실패했습니다. 다시 시도해주세요.");
        }
    }

    @Transactional
    public Long returnBook(BookLoanRecordRequestDto bookLoanRecordRequestDto) {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = memberService.getCurrentMember().getId();
        Book book = bookService.getBookByIdOrThrow(bookId);
        Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId)) {
            throw new InvalidBorrowerException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(book, borrower);
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime extensionDate = bookLoanRecord.getDueDate();
        if (currentDate.isAfter(extensionDate)) {
            handleOverdueAndSuspension(borrower, ChronoUnit.DAYS.between(extensionDate, currentDate));
        }
        book.setBorrower(null);
        bookRepository.save(book);
        bookLoanRecord.setReturnedAt(currentDate);
        Long id = bookLoanRecordRepository.save(bookLoanRecord).getId();
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(borrowerId)
                .content("[" + book.getTitle() + "] 도서 반납이 완료되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
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
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(book, borrower);
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

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(borrowerId)
                .content("[" + book.getTitle() + "] 도서 대출 연장이 완료되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    private void handleOverdueAndSuspension(Member member, long overdueDays) {
        LocalDateTime suspensionEndDate = LocalDateTime.now().plusDays(overdueDays * 7);
        member.setLoanSuspensionDate(suspensionEndDate);
        memberService.saveMember(member);
    }

    public PagedResponseDto<BookLoanRecordResponseDto> getBookLoanRecords(Pageable pageable) {
        Page<BookLoanRecord> bookLoanRecords = bookLoanRecordRepository.findAllByOrderByBorrowedAtDesc(pageable);
        return new PagedResponseDto<>(bookLoanRecords.map(BookLoanRecordResponseDto::of));
    }

    public PagedResponseDto<BookLoanRecordResponseDto> searchBookLoanRecord(Long bookId, String borrowerId, Pageable pageable) {
        Page<BookLoanRecord> bookLoanRecords;
        if (bookId != null && borrowerId != null) {
            bookLoanRecords = getBookLoanRecordByBookIdAndBorrowerId(bookId, borrowerId, pageable);
        } else if (bookId != null) {
            bookLoanRecords = getBookLoanRecordByBookId(bookId, pageable);
        } else if (borrowerId != null) {
            bookLoanRecords = getBookLoanRecordByBorrowerId(borrowerId, pageable);
        } else {
            throw new IllegalArgumentException("적어도 bookId 또는 borrowerId 중 하나를 제공해야 합니다.");
        }
        if (bookLoanRecords.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return new PagedResponseDto<>(bookLoanRecords.map(BookLoanRecordResponseDto::of));
    }

    public PagedResponseDto<BookLoanRecordResponseDto> getUnreturnedBooks(Pageable pageable) {
        Page<BookLoanRecord> unreturnedBookLoanRecords = getBookLoanRecordByReturnedAtIsNull(pageable);
        return new PagedResponseDto<>(unreturnedBookLoanRecords.map(BookLoanRecordResponseDto::of));
    }

    public BookLoanRecord getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(Book book, Member borrower) {
        return bookLoanRecordRepository.findByBookAndBorrowerAndReturnedAtIsNull(book, borrower)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

    private Page<BookLoanRecord> getBookLoanRecordByBookId(Long bookId, Pageable pageable) {
        return bookLoanRecordRepository.findByBook_IdOrderByBorrowedAtDesc(bookId, pageable);
    }

    private Page<BookLoanRecord> getBookLoanRecordByBorrowerId(String borrowerId, Pageable pageable) {
        return bookLoanRecordRepository.findByBorrower_IdOrderByBorrowedAtDesc(borrowerId, pageable);
    }

    private Page<BookLoanRecord> getBookLoanRecordByBookIdAndBorrowerId(Long bookId, String borrowerId, Pageable pageable) {
        return bookLoanRecordRepository.findByBook_IdAndBorrower_IdOrderByBorrowedAtDesc(bookId, borrowerId, pageable);
    }

    private Page<BookLoanRecord> getBookLoanRecordByReturnedAtIsNull(Pageable pageable) {
        return bookLoanRecordRepository.findByReturnedAtIsNullOrderByBorrowedAtDesc(pageable);
    }

}