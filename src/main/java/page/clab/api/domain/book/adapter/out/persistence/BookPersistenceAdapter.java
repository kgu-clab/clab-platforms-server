package page.clab.api.domain.book.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.application.port.out.RemoveBookPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookPort;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookPersistenceAdapter implements
        RegisterBookPort,
        RemoveBookPort,
        RetrieveBookPort {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Book save(Book book) {
        BookJpaEntity entity = bookMapper.toJpaEntity(book);
        BookJpaEntity savedEntity = bookRepository.save(entity);
        return bookMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Book book) {
        bookRepository.deleteById(book.getId());
    }

    @Override
    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toDomain);
    }

    @Override
    public Book findByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Book] id: " + bookId + "에 해당하는 책이 존재하지 않습니다."));
    }

    @Override
    public Page<Book> findAllByIsDeletedTrue(Pageable pageable) {
        return bookRepository.findAllByIsDeletedTrue(pageable)
                .map(bookMapper::toDomain);
    }

    @Override
    public Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable) {
        return bookRepository.findByConditions(title, category, publisher, borrowerId, borrowerName, pageable)
                .map(bookMapper::toDomain);
    }

    @Override
    public int countByBorrowerId(String borrowerId) {
        return bookRepository.countByBorrowerId(borrowerId);
    }
}
