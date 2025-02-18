package page.clab.api.domain.members.blog.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.blog.application.port.in.RemoveBlogUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Members - Blog", description = "기술 블로그")
public class BlogRemoveController {

    private final RemoveBlogUseCase removeBlogUseCase;

    @Operation(summary = "[A] 블로그 포스트 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{blogId}")
    public ApiResponse<Long> removeBlog(
        @PathVariable(name = "blogId") Long blogId
    ) {
        Long id = removeBlogUseCase.removeBlog(blogId);
        return ApiResponse.success(id);
    }
}
