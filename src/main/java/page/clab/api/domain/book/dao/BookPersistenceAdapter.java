package page.clab.api.domain.book.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.book.application.port.out.CountBooksByBorrowerPort;
import page.clab.api.domain.book.application.port.out.LoadBookPort;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.application.port.out.RemoveBookPort;
import page.clab.api.domain.book.application.port.out.RetrieveBooksByConditionsPort;
import page.clab.api.domain.book.application.port.out.RetrieveDeletedBooksPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookPersistenceAdapter implements
        RegisterBookPort,
        RemoveBookPort,
        LoadBookPort,
        RetrieveDeletedBooksPort,
        RetrieveBooksByConditionsPort,
        CountBooksByBorrowerPort {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public Book findByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("[Book] id: " + bookId + "에 해당하는 책이 존재하지 않습니다."));
    }

    @Override
    public Page<Book> findAllByIsDeletedTrue(Pageable pageable) {
        return bookRepository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        return bookRepository.findByConditions(title, category, publisher, borrowerId, borrowerName, pageable);
    }

    @Override
    public int countByBorrowerId(String borrowerId) {
        return bookRepository.countByBorrowerId(borrowerId);
    }
}
