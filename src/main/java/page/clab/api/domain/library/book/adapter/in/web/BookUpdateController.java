package page.clab.api.domain.library.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.book.application.dto.request.BookUpdateRequestDto;
import page.clab.api.domain.library.book.application.port.in.UpdateBookUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Library - Book", description = "도서관 도서")
public class BookUpdateController {

    private final UpdateBookUseCase updateBookUseCase;

    @Operation(summary = "[A] 도서 정보 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @PatchMapping("")
    public ApiResponse<Long> updateBookInfo(
            @RequestParam(name = "bookId") Long bookId,
            @Valid @RequestBody BookUpdateRequestDto requestDto
    ) {
        Long id = updateBookUseCase.updateBookInfo(bookId, requestDto);
        return ApiResponse.success(id);
    }
}
