package page.clab.api.domain.community.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.comment.application.port.in.RemoveCommentUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Community - Comment", description = "커뮤니티 댓글")
public class CommentRemoveController {

    private final RemoveCommentUseCase removeCommentUseCase;

    @Operation(summary = "[U] 댓글 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{commentId}")
    public ApiResponse<Long> removeComment(
        @PathVariable(name = "commentId") Long commentId
    ) throws PermissionDeniedException {
        Long id = removeCommentUseCase.removeComment(commentId);
        return ApiResponse.success(id);
    }
}
