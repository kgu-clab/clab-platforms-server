package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.RetrieveOverdueBookLoanRecordsUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class OverdueBookLoanRecordsRetrievalService implements RetrieveOverdueBookLoanRecordsUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookLoanRecordOverdueResponseDto> retrieveOverdueBookLoanRecords(Pageable pageable) {
        Page<BookLoanRecordOverdueResponseDto> overdueBookLoanRecords = retrieveBookLoanRecordPort.findOverdueBookLoanRecords(
            pageable);
        return new PagedResponseDto<>(overdueBookLoanRecords);
    }
}
