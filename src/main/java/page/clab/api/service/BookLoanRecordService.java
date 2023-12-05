package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.BookAlreadyBorrowedException;
import page.clab.api.exception.InvalidBorrowerException;
import page.clab.api.exception.LoanSuspensionException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.OverdueException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.BookLoanRecordRepository;
import page.clab.api.repository.BookRepository;
import page.clab.api.type.dto.BookLoanRecordRequestDto;
import page.clab.api.type.dto.BookLoanRecordResponseDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Book;
import page.clab.api.type.entity.BookLoanRecord;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.Role;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLoanRecordService {

    private final BookService bookService;

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final BookRepository bookRepository;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    public void borrowBook(BookLoanRecordRequestDto bookLoanRecordRequestDto) {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = bookLoanRecordRequestDto.getBorrowerId();
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
                .build();
        bookLoanRecordRepository.save(bookLoanRecord);

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(borrowerId)
                .content("도서 대출이 완료되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);

        List<Member> superMembers = memberService.getMembersByRole(Role.SUPER);
        for (Member superMember : superMembers) {
            NotificationRequestDto notificationRequestDtoForSuper = NotificationRequestDto.builder()
                    .memberId(superMember.getId())
                    .content(borrower.getName() + "님이 " + book.getTitle() +  " 도서를 대출하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDtoForSuper);
        }
    }

    @Transactional
    public void returnBook(BookLoanRecordRequestDto bookLoanRecordRequestDto) {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = bookLoanRecordRequestDto.getBorrowerId();
        Book book = bookService.getBookByIdOrThrow(bookId);
        Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId)) {
            throw new InvalidBorrowerException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(book, borrower);
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime borrowedDate = bookLoanRecord.getBorrowedAt();
        LocalDateTime extensionDate = bookLoanRecord.getLoanExtensionDate();
        if (bookLoanRecord.getLoanExtensionDate() == null) {
            long overdueDays = ChronoUnit.DAYS.between(borrowedDate, currentDate);
            if (overdueDays > 21) {
                handleOverdueAndSuspension(borrower, overdueDays);
            }
        } else {
            long overdueDays = ChronoUnit.DAYS.between(extensionDate, currentDate);
            if (overdueDays > 14) {
                handleOverdueAndSuspension(borrower, overdueDays);
            }
        }
        book.setBorrower(null);
        bookRepository.save(book);
        bookLoanRecord.setReturnedAt(currentDate);
        bookLoanRecordRepository.save(bookLoanRecord);

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(borrowerId)
                .content("도서 반납이 완료되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);

        List<Member> superMembers = memberService.getMembersByRole(Role.SUPER);
        for (Member superMember : superMembers) {
            NotificationRequestDto notificationRequestDtoForSuper = NotificationRequestDto.builder()
                    .memberId(superMember.getId())
                    .content(borrower.getName() + "님이 " + book.getTitle() +  " 도서를 반납하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDtoForSuper);
        }
    }

    @Transactional
    public void extendBookLoan(BookLoanRecordRequestDto bookLoanRecordRequestDto) {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = bookLoanRecordRequestDto.getBorrowerId();
        Book book = bookService.getBookByIdOrThrow(bookId);
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId)) {
            throw new InvalidBorrowerException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
        Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
        if (borrower.getLoanSuspensionDate() != null && LocalDateTime.now().isBefore(borrower.getLoanSuspensionDate())) {
            throw new LoanSuspensionException("대출 정지 중입니다. 연장할 수 없습니다.");
        }
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(book, borrower);
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime borrowedDate = bookLoanRecord.getBorrowedAt();
        long overdueDays = ChronoUnit.DAYS.between(borrowedDate, currentDate);
        if (bookLoanRecord.getLoanExtensionDate() == null) {
            if (overdueDays <= 21) {
                LocalDateTime extensionDate = borrowedDate.plusWeeks(3);
                bookLoanRecord.setLoanExtensionDate(extensionDate);
            } else {
                throw new OverdueException("대출 연장이 불가능합니다.");
            }
        } else {
            if (overdueDays <= 35) {
                LocalDateTime extensionDate = borrowedDate.plusWeeks(5);
                bookLoanRecord.setLoanExtensionDate(extensionDate);
            } else {
                throw new OverdueException("대출 연장이 불가능합니다.");
            }
        }
        bookLoanRecordRepository.save(bookLoanRecord);

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(borrowerId)
                .content("도서 대출 연장이 완료되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);

        List<Member> superMembers = memberService.getMembersByRole(Role.SUPER);
        for (Member superMember : superMembers) {
            NotificationRequestDto notificationRequestDtoForSuper = NotificationRequestDto.builder()
                    .memberId(superMember.getId())
                    .content(borrower.getName() + "님이 " + book.getTitle() +  " 도서를 연장하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDtoForSuper);
        }
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
