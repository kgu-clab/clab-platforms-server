package page.clab.api.domain.library.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.book.application.port.in.RemoveBookUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Library - Book", description = "도서관 도서")
public class BookRemoveController {

    private final RemoveBookUseCase removeBookUseCase;

    @Operation(summary = "[A] 도서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}")
    public ApiResponse<Long> removeBook(
        @PathVariable(name = "bookId") Long bookId
    ) {
        Long id = removeBookUseCase.removeBook(bookId);
        return ApiResponse.success(id);
    }
}
