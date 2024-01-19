package page.clab.api.domain.book.application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.dto.request.BookRequestDto;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.dto.PagedResponseDto;
import page.clab.api.domain.book.domain.Book;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final EntityManager entityManager;

    public Long createBook(BookRequestDto bookRequestDto) {
        Book book = Book.of(bookRequestDto);
        return bookRepository.save(book).getId();
    }

    public PagedResponseDto<BookResponseDto> getBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(books.map(BookResponseDto::of));
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
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), distinctBooks.size());
        Page<Book> bookPage = new PageImpl<>(distinctBooks.subList(start, end), pageable, distinctBooks.size());
        return new PagedResponseDto<>(bookPage.map(BookResponseDto::of));
    }

    public Long updateBookInfo(Long bookId, BookRequestDto bookRequestDto) {
        Book book = getBookByIdOrThrow(bookId);
        Book updatedBook = Book.of(bookRequestDto);
        updatedBook.setId(book.getId());
        updatedBook.setCreatedAt(book.getCreatedAt());
        updatedBook.setBorrower(book.getBorrower());
        return bookRepository.save(updatedBook).getId();
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

}
