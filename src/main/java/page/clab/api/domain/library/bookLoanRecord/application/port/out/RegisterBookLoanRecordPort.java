package page.clab.api.domain.library.bookLoanRecord.application.port.out;

import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;

public interface RegisterBookLoanRecordPort {

    BookLoanRecord save(BookLoanRecord bookLoanRecord);
}
