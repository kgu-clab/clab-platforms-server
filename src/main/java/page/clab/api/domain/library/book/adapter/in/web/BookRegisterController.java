package page.clab.api.domain.library.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.library.book.application.dto.request.BookRequestDto;
import page.clab.api.domain.library.book.application.port.in.RegisterBookUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Library - Book", description = "도서관 도서")
public class BookRegisterController {

    private final RegisterBookUseCase registerBookUseCase;

    @Operation(summary = "[A] 도서 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<Long> registerBook(
        @Valid @RequestBody BookRequestDto requestDto
    ) {
        Long id = registerBookUseCase.registerBook(requestDto);
        return ApiResponse.success(id);
    }
}
