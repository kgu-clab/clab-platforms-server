package page.clab.api.domain.library.book.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.library.book.application.dto.response.BookResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBooksByConditionsUseCase {

    PagedResponseDto<BookResponseDto> retrieveBooks(String title, String category, String publisher, String borrowerId,
        String borrowerName, Pageable pageable);
}
