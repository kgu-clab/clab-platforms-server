package page.clab.api.domain.book.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface BooksByConditionsRetrievalUseCase {
    PagedResponseDto<BookResponseDto> retrieve(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);
}
