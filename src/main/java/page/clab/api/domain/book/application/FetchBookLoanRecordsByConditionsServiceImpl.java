package page.clab.api.domain.book.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.book.dao.BookLoanRecordRepository;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchBookLoanRecordsByConditionsServiceImpl implements FetchBookLoanRecordsByConditionsService {

    private final BookLoanRecordRepository bookLoanRecordRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BookLoanRecordResponseDto> execute(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable) {
        Page<BookLoanRecordResponseDto> bookLoanRecords = bookLoanRecordRepository.findByConditions(bookId, borrowerId, status, pageable);
        return new PagedResponseDto<>(bookLoanRecords);
    }
}