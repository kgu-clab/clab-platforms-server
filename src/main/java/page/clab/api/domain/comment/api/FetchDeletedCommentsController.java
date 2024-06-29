package page.clab.api.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.comment.application.FetchDeletedCommentsService;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글")
public class FetchDeletedCommentsController {

    private final FetchDeletedCommentsService fetchDeletedCommentsService;

    @Operation(summary = "[S] 게시글의 삭제된 댓글 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @GetMapping("/deleted/{boardId}")
    public ApiResponse<PagedResponseDto<DeletedCommentResponseDto>> getDeletedComments(
            @PathVariable(name = "boardId") Long boardId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DeletedCommentResponseDto> comments = fetchDeletedCommentsService.execute(boardId, pageable);
        return ApiResponse.success(comments);
    }
}