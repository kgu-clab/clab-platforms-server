package page.clab.api.domain.book.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.RejectBookLoanUseCase;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.book.domain.BookLoanRecord;

@Service
@RequiredArgsConstructor
public class BookLoanRejectionService implements RejectBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;

    @Transactional
    @Override
    public Long rejectBookLoan(Long bookLoanRecordId) {
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.findByIdOrThrow(bookLoanRecordId);
        bookLoanRecord.reject();
        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}
