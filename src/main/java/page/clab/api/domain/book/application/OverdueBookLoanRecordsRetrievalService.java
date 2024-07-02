package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.port.in.OverdueBookLoanRecordsRetrievalUseCase;
import page.clab.api.domain.book.application.port.out.RetrieveOverdueBookLoanRecordsPort;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class OverdueBookLoanRecordsRetrievalService implements OverdueBookLoanRecordsRetrievalUseCase {

    private final RetrieveOverdueBookLoanRecordsPort retrieveOverdueBookLoanRecordsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookLoanRecordOverdueResponseDto> retrieve(Pageable pageable) {
        Page<BookLoanRecordOverdueResponseDto> overdueBookLoanRecords = retrieveOverdueBookLoanRecordsPort.findOverdueBookLoanRecords(pageable);
        return new PagedResponseDto<>(overdueBookLoanRecords);
    }
}