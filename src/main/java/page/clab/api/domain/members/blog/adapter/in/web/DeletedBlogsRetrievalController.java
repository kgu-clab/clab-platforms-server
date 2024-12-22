package page.clab.api.domain.members.blog.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.members.blog.application.port.in.RetrieveDeletedBlogsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Members - Blog", description = "기술 블로그")
public class DeletedBlogsRetrievalController {

    private final RetrieveDeletedBlogsUseCase retrieveDeletedBlogsUseCase;

    @Operation(summary = "[S] 삭제된 블로그 포스트 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("/deleted")
    public ApiResponse<PagedResponseDto<BlogDetailsResponseDto>> retrieveDeletedBlogs(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BlogDetailsResponseDto> blogs = retrieveDeletedBlogsUseCase.retrieveDeletedBlogs(pageable);
        return ApiResponse.success(blogs);
    }
}
