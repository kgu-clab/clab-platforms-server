package page.clab.api.domain.library.book.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.library.book.application.dto.response.BookDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedBooksUseCase {
    PagedResponseDto<BookDetailsResponseDto> retrieveDeletedBooks(Pageable pageable);
}
