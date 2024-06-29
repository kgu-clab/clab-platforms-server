package page.clab.api.domain.book.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchBooksByConditionsService {
    PagedResponseDto<BookResponseDto> execute(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);
}
