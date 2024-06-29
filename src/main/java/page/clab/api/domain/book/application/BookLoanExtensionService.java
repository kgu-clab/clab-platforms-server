package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;

public interface BookLoanExtensionService {
    Long extend(BookLoanRecordRequestDto requestDto);
}