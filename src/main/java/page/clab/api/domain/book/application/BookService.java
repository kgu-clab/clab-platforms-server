package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookService {

    private final MemberLookupService memberLookupService;

    private final BookRepository bookRepository;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional
    public Long createBook(BookRequestDto requestDto) {
        Book book = BookRequestDto.toEntity(requestDto);
        return bookRepository.save(book).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BookResponseDto> getBooksByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        Page<Book> books = bookRepository.findByConditions(title, category, publisher, borrowerId, borrowerName, pageable);
        return new PagedResponseDto<>(books.map(this::mapToBookResponseDto));
    }

    @Transactional(readOnly = true)
    public BookDetailsResponseDto getBookDetails(Long bookId) {
        MemberBasicInfoDto currentMemberInfo = memberLookupService.getCurrentMemberBasicInfo();
        Book book = getBookByIdOrThrow(bookId);
        return mapToBookDetailsResponseDto(book, currentMemberInfo.getMemberName());
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BookDetailsResponseDto> getDeletedBooks(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberLookupService.getCurrentMemberBasicInfo();
        Page<Book> books = bookRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(books.map((book) -> mapToBookDetailsResponseDto(book, currentMemberInfo.getMemberName())));
    }

    @Transactional
    public Long updateBookInfo(Long bookId, BookUpdateRequestDto bookUpdateRequestDto) {
        Book book = getBookByIdOrThrow(bookId);
        book.update(bookUpdateRequestDto);
        return bookRepository.save(book).getId();
    }

    @Transactional
    public Long deleteBook(Long bookId) {
        Book book = getBookByIdOrThrow(bookId);
        book.delete();
        bookRepository.save(book);
        return book.getId();
    }

    public Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }

    public BookLoanRecord getBookLoanRecordByBookAndReturnedAtIsNull(Book book) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNullAndStatus(book, BookLoanStatus.APPROVED)
                .orElse(null);
    }

    public int getNumberOfBooksBorrowedByMember(String member) {
        return bookRepository.countByBorrowerId(member);
    }

    private LocalDateTime getDueDateForBook(Book book) {
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNull(book);
        return bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
    }

    @NotNull
    private BookResponseDto mapToBookResponseDto(Book book) {
        MemberBasicInfoDto currentMemberInfo = memberLookupService.getCurrentMemberBasicInfo();
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookResponseDto.toDto(book, currentMemberInfo.getMemberName(), dueDate);
    }

    @NotNull
    private BookDetailsResponseDto mapToBookDetailsResponseDto(Book book, String borrowerName) {
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookDetailsResponseDto.toDto(book, borrowerName, dueDate);
    }

}
