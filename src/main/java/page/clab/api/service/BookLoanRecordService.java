package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.BookLoanRecordRepository;
import page.clab.api.repository.BookRepository;
import page.clab.api.type.dto.BookLoanRecordRequestDto;
import page.clab.api.type.dto.BookLoanRecordResponseDto;
import page.clab.api.type.entity.Book;
import page.clab.api.type.entity.BookLoanRecord;
import page.clab.api.type.entity.Member;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanRecordService {

    private final BookService bookService;

    private final MemberService memberService;

    private final BookRepository bookRepository;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    public void borrowBook(BookLoanRecordRequestDto bookLoanRecordRequestDto) throws PermissionDeniedException {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = bookLoanRecordRequestDto.getBorrowerId();
        Book book = bookService.getBookByIdOrThrow(bookId);
        if (book.getBorrower() != null) {
            throw new PermissionDeniedException("이미 대출 중인 도서입니다.");
        }
        Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
        book.setBorrower(borrower);
        bookRepository.save(book);
        BookLoanRecord bookLoanRecord = BookLoanRecord.builder()
                .book(book)
                .borrower(borrower)
                .borrowedAt(LocalDateTime.now())
                .build();
        bookLoanRecordRepository.save(bookLoanRecord);
    }

    @Transactional
    public void returnBook(BookLoanRecordRequestDto bookLoanRecordRequestDto) throws PermissionDeniedException {
        Long bookId = bookLoanRecordRequestDto.getBookId();
        String borrowerId = bookLoanRecordRequestDto.getBorrowerId();
        Book book = bookService.getBookByIdOrThrow(bookId);
        Member borrower = memberService.getMemberByIdOrThrow(borrowerId);
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId)) {
            throw new PermissionDeniedException("대출한 도서와 회원 정보가 일치하지 않습니다.");
        }
        book.setBorrower(null);
        bookRepository.save(book);
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(book, borrower);
        bookLoanRecord.setReturnedAt(LocalDateTime.now());
        bookLoanRecordRepository.save(bookLoanRecord);
    }

    public List<BookLoanRecordResponseDto> getBookLoanRecords() {
        List<BookLoanRecord> bookLoanRecords = bookLoanRecordRepository.findAll();
        return bookLoanRecords.stream()
                .map(BookLoanRecordResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<BookLoanRecordResponseDto> searchBookLoanRecord(Long bookId, String borrowerId) {
        List<BookLoanRecord> bookLoanRecords = new ArrayList<>();
        if (bookId != null && borrowerId != null) {
            bookLoanRecords = getBookLoanRecordByBookIdAndBorrowerId(bookId, borrowerId);
        } else if (bookId != null) {
            bookLoanRecords = getBookLoanRecordByBookId(bookId);
        } else if (borrowerId != null) {
            bookLoanRecords = getBookLoanRecordByBorrowerId(borrowerId);
        } else {
            throw new IllegalArgumentException("적어도 bookId 또는 borrowerId 중 하나를 제공해야 합니다.");
        }
        if (bookLoanRecords.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return bookLoanRecords.stream()
                .map(BookLoanRecordResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<BookLoanRecordResponseDto> getUnreturnedBooks() {
        List<BookLoanRecord> unreturnedBookLoanRecords = bookLoanRecordRepository.findByReturnedAtIsNull();
        return unreturnedBookLoanRecords.stream()
                .map(BookLoanRecordResponseDto::of)
                .collect(Collectors.toList());
    }

    private List<BookLoanRecord> getBookLoanRecordByBookId(Long bookId) {
        return bookLoanRecordRepository.findByBook_Id(bookId);
    }

    private List<BookLoanRecord> getBookLoanRecordByBorrowerId(String borrowerId) {
        return bookLoanRecordRepository.findByBorrower_Id(borrowerId);
    }

    private List<BookLoanRecord> getBookLoanRecordByBookIdAndBorrowerId(Long bookId, String borrowerId) {
        return bookLoanRecordRepository.findByBook_IdAndBorrower_Id(bookId, borrowerId);
    }

    public BookLoanRecord getBookLoanRecordByBookAndBorrowerAndReturnedAtIsNull(Book book, Member borrower) {
        return bookLoanRecordRepository.findByBookAndBorrowerAndReturnedAtIsNull(book, borrower)
                .orElseThrow(() -> new NotFoundException("해당 도서 대출 기록이 없습니다."));
    }

}
