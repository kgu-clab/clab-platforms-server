package page.clab.api.domain.comment.application.port.in;

public interface CommentLikeToggleUseCase {
    Long toggle(Long commentId);
}
