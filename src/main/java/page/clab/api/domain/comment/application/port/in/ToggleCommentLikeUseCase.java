package page.clab.api.domain.comment.application.port.in;

public interface ToggleCommentLikeUseCase {
    Long toggle(Long commentId);
}
