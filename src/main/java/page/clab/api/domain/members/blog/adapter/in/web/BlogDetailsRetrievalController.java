package page.clab.api.domain.members.blog.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.members.blog.application.port.in.RetrieveBlogDetailsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Members - Blog", description = "기술 블로그")
public class BlogDetailsRetrievalController {

    private final RetrieveBlogDetailsUseCase retrieveBlogDetailsUseCase;

    @Operation(summary = "[G] 블로그 포스트 상세 조회", description = "ROLE_GUEST 이상의 권한이 필요함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/{blogId}")
    public ApiResponse<BlogDetailsResponseDto> retrieveBlogDetails(
        @PathVariable(name = "blogId") Long blogId
    ) {
        BlogDetailsResponseDto blog = retrieveBlogDetailsUseCase.retrieveBlogDetails(blogId);
        return ApiResponse.success(blog);
    }
}
