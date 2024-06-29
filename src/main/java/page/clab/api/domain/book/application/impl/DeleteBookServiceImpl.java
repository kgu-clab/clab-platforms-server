package page.clab.api.domain.book.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.DeleteBookService;
import page.clab.api.domain.book.dao.BookRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteBookServiceImpl implements DeleteBookService {

    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Long execute(Long bookId) {
        Book book = getBookByIdOrThrow(bookId);
        book.delete();
        bookRepository.save(book);
        return book.getId();
    }

    private Book getBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 도서가 없습니다."));
    }
}