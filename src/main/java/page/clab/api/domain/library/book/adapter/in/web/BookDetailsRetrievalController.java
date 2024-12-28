package page.clab.api.domain.library.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.book.application.dto.response.BookDetailsResponseDto;
import page.clab.api.domain.library.book.application.port.in.RetrieveBookDetailsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Library - Book", description = "도서관 도서")
public class BookDetailsRetrievalController {

    private final RetrieveBookDetailsUseCase retrieveBookDetailsUseCase;

    @Operation(summary = "[G] 도서 상세 정보", description = "ROLE_GUEST 이상의 권한이 필요함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/{bookId}")
    public ApiResponse<BookDetailsResponseDto> retrieveBookDetails(
        @PathVariable(name = "bookId") Long bookId
    ) {
        BookDetailsResponseDto book = retrieveBookDetailsUseCase.retrieveBookDetails(bookId);
        return ApiResponse.success(book);
    }
}
