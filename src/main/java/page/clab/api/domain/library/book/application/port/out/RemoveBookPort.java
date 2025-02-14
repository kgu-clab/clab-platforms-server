package page.clab.api.domain.library.book.application.port.out;

import page.clab.api.domain.library.book.domain.Book;

public interface RemoveBookPort {

    void delete(Book book);
}
