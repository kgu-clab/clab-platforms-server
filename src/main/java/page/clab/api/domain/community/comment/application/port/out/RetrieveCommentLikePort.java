package page.clab.api.domain.community.comment.application.port.out;

import page.clab.api.domain.community.comment.domain.CommentLike;

import java.util.Optional;

public interface RetrieveCommentLikePort {
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, String memberId);
}
