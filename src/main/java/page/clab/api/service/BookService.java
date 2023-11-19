package page.clab.api.service;

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
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BookRepository;
import page.clab.api.type.dto.BookRequestDto;
import page.clab.api.type.dto.BookResponseDto;
import page.clab.api.type.entity.Book;

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

    public List<BookResponseDto> getBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(BookResponseDto::of).getContent();
    }

    public List<BookResponseDto> searchBook(String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> bookRoot = criteriaQuery.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            String keywordLowerCase = "%" + keyword.toLowerCase() + "%";
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(bookRoot.get("category")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(bookRoot.get("title")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(bookRoot.get("author")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(bookRoot.get("publisher")), keywordLowerCase),
                    criteriaBuilder.equal(criteriaBuilder.lower(bookRoot.get("borrower").get("id")), keyword.toLowerCase())
            ));
        }
        criteriaQuery.select(bookRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Book> query = entityManager.createQuery(criteriaQuery);
        List<Book> books = query.getResultList();
        Set<Book> uniqueBooks = new LinkedHashSet<>(books);
        List<Book> distinctBooks = new ArrayList<>(uniqueBooks);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), distinctBooks.size());
        Page<Book> bookPage = new PageImpl<>(distinctBooks.subList(start, end), pageable, distinctBooks.size());
        return bookPage.map(BookResponseDto::of).getContent();
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
