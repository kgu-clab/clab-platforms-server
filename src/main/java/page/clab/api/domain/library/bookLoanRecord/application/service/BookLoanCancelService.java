package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.CancelBookLoanUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RegisterBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanRecord;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class BookLoanCancelService implements CancelBookLoanUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final RegisterBookLoanRecordPort registerBookLoanRecordPort;

    @Transactional
    @Override
    public Long cancelBookLoan(Long bookLoanRecordId) {
        BookLoanRecord bookLoanRecord = retrieveBookLoanRecordPort.getById(bookLoanRecordId);
        bookLoanRecord.validateAccessPermission(externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo());

        bookLoanRecord.cancel();

        return registerBookLoanRecordPort.save(bookLoanRecord).getId();
    }
}
