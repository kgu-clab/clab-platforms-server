package page.clab.api.domain.community.comment.application.port.in;

public interface ToggleCommentLikeUseCase {

    Long toggleLikeStatus(Long commentId);
}
