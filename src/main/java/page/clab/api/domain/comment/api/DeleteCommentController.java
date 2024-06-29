package page.clab.api.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.comment.application.DeleteCommentService;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글")
public class DeleteCommentController {

    private final DeleteCommentService deleteCommentService;

    @Operation(summary = "[U] 댓글 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{commentId}")
    public ApiResponse<Long> deleteComment(
            @PathVariable(name = "commentId") Long commentId
    ) throws PermissionDeniedException {
        Long id = deleteCommentService.execute(commentId);
        return ApiResponse.success(id);
    }
}