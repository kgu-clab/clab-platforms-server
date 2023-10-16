package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BookRepository;
import page.clab.api.type.dto.BookRequestDto;
import page.clab.api.type.dto.BookResponseDto;
import page.clab.api.type.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final MemberService memberService;

    private final BookRepository bookRepository;

    private final EntityManager entityManager;

    public void createBook(BookRequestDto bookRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Book book = Book.of(bookRequestDto);
        bookRepository.save(book);
    }

    public List<BookResponseDto> getBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<BookResponseDto> searchBook(String category, String title, String author, String publisher, String borrowerId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> bookRoot = criteriaQuery.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();
        if (category != null) {
            predicates.add(criteriaBuilder.equal(bookRoot.get("category"), category));
        }
        if (title != null) {
            predicates.add(criteriaBuilder.like(bookRoot.get("title"), "%" + title + "%"));
        }
        if (author != null) {
            predicates.add(criteriaBuilder.like(bookRoot.get("author"), "%" + author + "%"));
        }
        if (publisher != null) {
            predicates.add(criteriaBuilder.equal(bookRoot.get("publisher"), publisher));
        }
        if (borrowerId != null) {
            predicates.add(criteriaBuilder.equal(bookRoot.get("borrowerId"), borrowerId));
        }

        criteriaQuery.select(bookRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Book> query = entityManager.createQuery(criteriaQuery);
        List<Book> books = query.getResultList();
        Set<Book> uniqueBooks = new HashSet<>(books);

        return uniqueBooks.stream()
                .map(BookResponseDto::of)
                .collect(Collectors.toList());
    }


    public void updateBookInfo(Long bookId, BookRequestDto bookRequestDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Book book = getBookByIdOrThrow(bookId);
        Book updatedBook = Book.of(bookRequestDto);
        updatedBook.setId(book.getId());
        updatedBook.setCreatedAt(book.getCreatedAt());
        updatedBook.setBorrower(book.getBorrower());
        bookRepository.save(updatedBook);
    }

    public void deleteBook(Long bookId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Book book = getBookByIdOrThrow(bookId);
        bookRepository.delete(book);
    }

    public Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }

}
