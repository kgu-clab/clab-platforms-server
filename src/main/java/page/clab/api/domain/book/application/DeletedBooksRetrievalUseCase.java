package page.clab.api.domain.book.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedBooksRetrievalUseCase {
    PagedResponseDto<BookDetailsResponseDto> retrieve(Pageable pageable);
}
