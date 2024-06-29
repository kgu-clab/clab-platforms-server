package page.clab.api.domain.blog.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.blog.application.CreateBlogService;
import page.clab.api.domain.blog.dto.request.BlogRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "블로그 포스트")
public class CreateBlogController {

    private final CreateBlogService createBlogService;

    @Operation(summary = "[A] 블로그 포스트 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createBlog(
            @Valid @RequestBody BlogRequestDto requestDto
    ) {
        Long id = createBlogService.execute(requestDto);
        return ApiResponse.success(id);
    }
}
