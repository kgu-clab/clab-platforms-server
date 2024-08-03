package page.clab.api.domain.library.bookLoanRecord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.library.bookLoanRecord.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.library.bookLoanRecord.application.port.in.RetrieveBookLoanRecordsByConditionsUseCase;
import page.clab.api.domain.library.bookLoanRecord.application.port.out.RetrieveBookLoanRecordPort;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BookLoanRecordsByConditionsRetrievalService implements RetrieveBookLoanRecordsByConditionsUseCase {

    private final RetrieveBookLoanRecordPort retrieveBookLoanRecordPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookLoanRecordResponseDto> retrieveBookLoanRecords(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable) {
        Page<BookLoanRecordResponseDto> bookLoanRecords = retrieveBookLoanRecordPort.findByConditions(bookId, borrowerId, status, pageable);
        return new PagedResponseDto<>(bookLoanRecords);
    }
}
