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
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookLoanRecordRepository bookLoanRecordRepository;

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
        Book book = getBookByIdOrThrow(bookId);
        return mapToBookDetailsResponseDto(book);
    }

    @Transactional
    public Long updateBookInfo(Long bookId, BookUpdateRequestDto bookUpdateRequestDto) {
        Book book = getBookByIdOrThrow(bookId);
        book.update(bookUpdateRequestDto);
        return bookRepository.save(book).getId();
    }

    public Long deleteBook(Long bookId) {
        Book book = getBookByIdOrThrow(bookId);
        bookRepository.delete(book);
        return book.getId();
    }

    public Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }

    public BookLoanRecord getBookLoanRecordByBookAndReturnedAtIsNull(Book book) {
        return bookLoanRecordRepository.findByBookAndReturnedAtIsNull(book)
                .orElse(null);
    }

    private LocalDateTime getDueDateForBook(Book book) {
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNull(book);
        return bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
    }

    @NotNull
    private BookResponseDto mapToBookResponseDto(Book book) {
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookResponseDto.toDto(book, dueDate);
    }

    @NotNull
    private BookDetailsResponseDto mapToBookDetailsResponseDto(Book book) {
        LocalDateTime dueDate = getDueDateForBook(book);
        return BookDetailsResponseDto.toDto(book, dueDate);
    }

}
