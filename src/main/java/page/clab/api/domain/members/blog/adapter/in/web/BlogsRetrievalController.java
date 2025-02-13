package page.clab.api.domain.members.blog.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.blog.application.dto.response.BlogResponseDto;
import page.clab.api.domain.members.blog.application.port.in.RetrieveBlogsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Members - Blog", description = "기술 블로그")
public class BlogsRetrievalController {

    private final RetrieveBlogsUseCase retrieveBlogsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 블로그 포스트 조회(제목, 작성자명 기준)", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
        "제목, 작성자명 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BlogResponseDto>> retrieveBlogs(
        @RequestParam(name = "title", required = false) String title,
        @RequestParam(name = "memberName", required = false) String memberName,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, BlogResponseDto.class);
        PagedResponseDto<BlogResponseDto> blogs = retrieveBlogsUseCase.retrieveBlogs(title, memberName, pageable);
        return ApiResponse.success(blogs);
    }
}
