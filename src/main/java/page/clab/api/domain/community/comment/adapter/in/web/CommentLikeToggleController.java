package page.clab.api.domain.community.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.comment.application.dto.response.CommentLikeToggleResponseDto;
import page.clab.api.domain.community.comment.application.port.in.ToggleCommentLikeUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Community - Comment", description = "커뮤니티 댓글")
public class CommentLikeToggleController {

    private final ToggleCommentLikeUseCase toggleCommentLikeUseCase;

    @Operation(summary = "[U] 댓글 좋아요 누르기/취소하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/likes/{commentId}")
    public ApiResponse<CommentLikeToggleResponseDto> toggleLikeStatus(
            @PathVariable(name = "commentId") Long commentId
    ) {
        CommentLikeToggleResponseDto responseDto = toggleCommentLikeUseCase.toggleLikeStatus(commentId);
        return ApiResponse.success(responseDto);
    }
}
