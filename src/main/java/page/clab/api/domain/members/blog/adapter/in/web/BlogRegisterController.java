package page.clab.api.domain.members.blog.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.blog.application.dto.request.BlogRequestDto;
import page.clab.api.domain.members.blog.application.port.in.RegisterBlogUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Members - Blog", description = "기술 블로그")
public class BlogRegisterController {

    private final RegisterBlogUseCase registerBlogUseCase;

    @Operation(summary = "[A] 블로그 포스트 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerBlog(
            @Valid @RequestBody BlogRequestDto requestDto
    ) {
        Long id = registerBlogUseCase.registerBlog(requestDto);
        return ApiResponse.success(id);
    }
}
