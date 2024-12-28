package page.clab.api.domain.community.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.comment.application.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.community.comment.application.port.in.RetrieveDeletedCommentsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Community - Comment", description = "커뮤니티 댓글")
public class DeletedCommentsRetrievalController {

    private final RetrieveDeletedCommentsUseCase retrieveDeletedCommentsUseCase;

    @Operation(summary = "[S] 게시글의 삭제된 댓글 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("/deleted/{boardId}")
    public ApiResponse<PagedResponseDto<DeletedCommentResponseDto>> retrieveDeletedComments(
        @PathVariable(name = "boardId") Long boardId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DeletedCommentResponseDto> comments = retrieveDeletedCommentsUseCase.retrieveDeletedComments(
            boardId, pageable);
        return ApiResponse.success(comments);
    }
}
