package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;

public interface ExtendBookLoanService {
    Long execute(BookLoanRecordRequestDto requestDto);
}