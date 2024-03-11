package page.clab.api.domain.book.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookLoanRecordRepository bookLoanRecordRepository;

    private final EntityManager entityManager;

    public Long createBook(BookRequestDto bookRequestDto) {
        Book book = Book.of(bookRequestDto);
        return bookRepository.save(book).getId();
    }

    public PagedResponseDto<BookResponseDto> getBooks(Pageable pageable) {
        List<Book> books = bookRepository.findAllByOrderByCreatedAtDesc();
        return getBookResponseDtoPagedResponseDto(pageable, books);
    }

    public BookResponseDto getBookDetails(Long bookId) {
        Book book = getBookByIdOrThrow(bookId);
        BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNull(book);
        LocalDateTime dueDate = bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
        return BookResponseDto.of(book, dueDate);
    }

    public PagedResponseDto<BookResponseDto> searchBook(String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            String keywordLowerCase = "%" + keyword.toLowerCase() + "%";
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher")), keywordLowerCase),
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("borrower").get("id")), keyword.toLowerCase())
            ));
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));
        TypedQuery<Book> query = entityManager.createQuery(criteriaQuery);
        List<Book> books = query.getResultList();
        Set<Book> uniqueBooks = new LinkedHashSet<>(books);
        List<Book> distinctBooks = new ArrayList<>(uniqueBooks);
        return getBookResponseDtoPagedResponseDto(pageable, distinctBooks);
    }

    @NotNull
    private PagedResponseDto<BookResponseDto> getBookResponseDtoPagedResponseDto(Pageable pageable, List<Book> books) {
        List<BookResponseDto> bookResponseDtos = books.stream()
                .map(book -> {
                    BookLoanRecord bookLoanRecord = getBookLoanRecordByBookAndReturnedAtIsNull(book);
                    LocalDateTime dueDate = bookLoanRecord != null ? bookLoanRecord.getDueDate() : null;
                    return BookResponseDto.of(book, dueDate);
                })
                .toList();
        return new PagedResponseDto<>(bookResponseDtos, pageable, books.size());
    }

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

}
