package page.clab.api.domain.blog.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.blog.application.BlogService;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.request.BlogRequestDto;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.blog.dto.response.BlogResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "블로그 포스트")
@Slf4j
public class BlogController {

    private final BlogService blogService;

    @Operation(summary = "[A] 블로그 포스트 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createBlog(
            @Valid @RequestBody BlogRequestDto requestDto
    ) {
        Long id = blogService.createBlog(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 블로그 포스트 조회(제목, 작성자명 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "제목, 작성자명 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BlogResponseDto>> getBlogsByConditions(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "memberName", required = false) String memberName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Blog.class);
        PagedResponseDto<BlogResponseDto> blogs = blogService.getBlogsByConditions(title, memberName, pageable);
        return ApiResponse.success(blogs);
    }

    @Operation(summary = "[U] 블로그 포스트 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{blogId}")
    public ApiResponse<BlogDetailsResponseDto> getBlogDetails(
            @PathVariable(name = "blogId") Long blogId
    ) {
        BlogDetailsResponseDto blog = blogService.getBlogDetails(blogId);
        return ApiResponse.success(blog);
    }

    @Operation(summary = "[A] 블로그 포스트 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{blogId}")
    public ApiResponse<Long> updateBlog(
            @PathVariable(name = "blogId") Long blogId,
            @Valid @RequestBody BlogUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = blogService.updateBlog(blogId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 블로그 포스트 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{blogId}")
    public ApiResponse<Long> deleteBlog(
            @PathVariable(name = "blogId") Long blogId
    ) throws PermissionDeniedException {
        Long id = blogService.deleteBlog(blogId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 블로그 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<BlogDetailsResponseDto>> getDeletedBlogs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BlogDetailsResponseDto> blogs = blogService.getDeletedBlogs(pageable);
        return ApiResponse.success(blogs);
    }

}
