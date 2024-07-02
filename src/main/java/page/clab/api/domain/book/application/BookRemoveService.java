package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.BookRemoveUseCase;
import page.clab.api.domain.book.application.port.out.LoadBookPort;
import page.clab.api.domain.book.application.port.out.RegisterBookPort;
import page.clab.api.domain.book.domain.Book;

@Service
@RequiredArgsConstructor
public class BookRemoveService implements BookRemoveUseCase {

    private final LoadBookPort loadBookPort;
    private final RegisterBookPort registerBookPort;

    @Transactional
    @Override
    public Long remove(Long bookId) {
        Book book = loadBookPort.findByIdOrThrow(bookId);
        book.delete();
        return registerBookPort.save(book).getId();
    }
}