package page.clab.api.domain.community.comment.application.port.in;

import page.clab.api.domain.community.comment.application.dto.response.CommentLikeToggleResponseDto;

public interface ToggleCommentLikeUseCase {

    CommentLikeToggleResponseDto toggleLikeStatus(Long commentId);
}
