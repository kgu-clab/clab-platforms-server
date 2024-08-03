package page.clab.api.external.library.book.application.port;

import page.clab.api.domain.library.book.domain.Book;

public interface ExternalRegisterBookUseCase {
    void save(Book book);
}
