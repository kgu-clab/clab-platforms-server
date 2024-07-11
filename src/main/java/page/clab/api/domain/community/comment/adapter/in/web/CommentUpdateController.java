package page.clab.api.domain.community.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.comment.application.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.community.comment.application.port.in.UpdateCommentUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Community - Comment", description = "커뮤니티 댓글")
public class CommentUpdateController {

    private final UpdateCommentUseCase updateCommentUseCase;

    @Operation(summary = "[U] 댓글 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PatchMapping("/{commentId}")
    public ApiResponse<Long> updateComment(
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = updateCommentUseCase.updateComment(commentId, requestDto);
        return ApiResponse.success(id);
    }
}
