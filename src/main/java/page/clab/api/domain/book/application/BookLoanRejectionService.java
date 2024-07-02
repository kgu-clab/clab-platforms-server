package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.BookLoanRejectionUseCase;
import page.clab.api.domain.book.application.port.out.LoadBookLoanRecordPort;
import page.clab.api.domain.book.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BookLoanRejectionService implements BookLoanRejectionUseCase {

    private final LoadBookLoanRecordPort loadBookLoanRecordPort;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long reject(Long bookLoanRecordId) {
        BookLoanRecord bookLoanRecord = loadBookLoanRecordPort.findByIdOrThrow(bookLoanRecordId);
        bookLoanRecord.reject();
        validationService.checkValid(bookLoanRecord);
        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}
