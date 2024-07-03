package page.clab.api.domain.blog.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.blog.application.port.in.RetrieveBlogDetailsUseCase;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "블로그 포스트")
public class BlogDetailsRetrievalController {

    private final RetrieveBlogDetailsUseCase retrieveBlogDetailsUseCase;

    @Operation(summary = "[U] 블로그 포스트 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{blogId}")
    public ApiResponse<BlogDetailsResponseDto> retrieveBlogDetails(
            @PathVariable(name = "blogId") Long blogId
    ) {
        BlogDetailsResponseDto blog = retrieveBlogDetailsUseCase.retrieve(blogId);
        return ApiResponse.success(blog);
    }
}