package page.clab.api.domain.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.comment.application.port.in.ToggleCommentLikeUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글")
public class CommentLikeToggleController {

    private final ToggleCommentLikeUseCase toggleCommentLikeUseCase;

    @Operation(summary = "[U] 댓글 좋아요 누르기/취소하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("/likes/{commentId}")
    public ApiResponse<Long> toggleLikeStatus(
            @PathVariable(name = "commentId") Long commentId
    ) {
        Long id = toggleCommentLikeUseCase.toggleLikeStatus(commentId);
        return ApiResponse.success(id);
    }
}
