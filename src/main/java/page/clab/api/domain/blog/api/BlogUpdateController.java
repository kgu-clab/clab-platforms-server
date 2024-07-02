package page.clab.api.domain.blog.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.blog.application.port.in.BlogUpdateUseCase;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "블로그 포스트")
public class BlogUpdateController {

    private final BlogUpdateUseCase blogUpdateUseCase;

    @Operation(summary = "[A] 블로그 포스트 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{blogId}")
    public ApiResponse<Long> updateBlog(
            @PathVariable(name = "blogId") Long blogId,
            @Valid @RequestBody BlogUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = blogUpdateUseCase.update(blogId, requestDto);
        return ApiResponse.success(id);
    }
}
