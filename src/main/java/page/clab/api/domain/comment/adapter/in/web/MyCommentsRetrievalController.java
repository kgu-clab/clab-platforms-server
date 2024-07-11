package page.clab.api.domain.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.comment.application.dto.response.CommentMyResponseDto;
import page.clab.api.domain.comment.application.port.in.RetrieveMyCommentsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글")
public class MyCommentsRetrievalController {

    private final RetrieveMyCommentsUseCase retrieveMyCommentsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 나의 댓글 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("/my-comments")
    public ApiResponse<PagedResponseDto<CommentMyResponseDto>> retrieveMyComments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, CommentMyResponseDto.class);
        PagedResponseDto<CommentMyResponseDto> comments = retrieveMyCommentsUseCase.retrieveMyComments(pageable);
        return ApiResponse.success(comments);
    }
}
