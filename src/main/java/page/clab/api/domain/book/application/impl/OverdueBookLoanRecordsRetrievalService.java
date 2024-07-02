package page.clab.api.domain.book.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.application.OverdueBookLoanRecordsRetrievalUseCase;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class OverdueBookLoanRecordsRetrievalService implements OverdueBookLoanRecordsRetrievalUseCase {

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookLoanRecordOverdueResponseDto> retrieve(Pageable pageable) {
        Page<BookLoanRecordOverdueResponseDto> overdueBookLoanRecords = bookLoanRecordRepository.findOverdueBookLoanRecords(pageable);
        return new PagedResponseDto<>(overdueBookLoanRecords);
    }
}