package page.clab.api.external.library.bookLoanRecord.application.port;

import java.time.LocalDateTime;

public interface ExternalRetrieveBookLoanRecordUseCase {

    LocalDateTime getDueDateForBook(Long bookId);

    String getBorrowerIdForBook(Long bookId);
}