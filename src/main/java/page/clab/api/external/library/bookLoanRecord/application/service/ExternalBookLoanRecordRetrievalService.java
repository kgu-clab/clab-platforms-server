package page.clab.api.external.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.external.library.bookLoanRecord.application.port.ExternalRetrieveBookLoanRecordUseCase;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExternalBookLoanRecordRetrievalService implements ExternalRetrieveBookLoanRecordUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;

    @Override
    public LocalDateTime getDueDateForBook(Long bookId) {
        return retrieveBookLoanRecordPort.findByBookIdAndReturnedAtIsNullAndStatus(bookId, BookLoanStatus.APPROVED)
                .map(BookLoanRecord::getDueDate)
                .orElse(null);
    }
}
