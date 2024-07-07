package page.clab.api.domain.book.application.port.out;

import page.clab.api.domain.book.domain.BookLoanRecord;

public interface RegisterBookLoanRecordPort {
    BookLoanRecord save(BookLoanRecord bookLoanRecord);
}