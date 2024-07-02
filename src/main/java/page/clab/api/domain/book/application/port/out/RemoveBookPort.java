package page.clab.api.domain.book.application.port.out;

import page.clab.api.domain.book.domain.Book;

public interface RemoveBookPort {
    void delete(Book book);
}

