package page.clab.api.domain.book.application.port.out;

public interface CountBooksByBorrowerPort {
    int countByBorrowerId(String borrowerId);
}
