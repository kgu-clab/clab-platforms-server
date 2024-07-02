package page.clab.api.domain.book.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.application.BookDetailsRetrievalUseCase;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서")
public class BookDetailsRetrievalController {

    private final BookDetailsRetrievalUseCase bookDetailsRetrievalUseCase;

    @Operation(summary = "[U] 도서 상세 정보", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{bookId}")
    public ApiResponse<BookDetailsResponseDto> retrieveBookDetails(
            @PathVariable(name = "bookId") Long bookId
    ) {
        BookDetailsResponseDto book = bookDetailsRetrievalUseCase.retrieve(bookId);
        return ApiResponse.success(book);
    }
}