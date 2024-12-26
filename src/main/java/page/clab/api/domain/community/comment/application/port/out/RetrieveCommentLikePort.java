package page.clab.api.domain.community.comment.application.port.out;

import java.util.Optional;
import page.clab.api.domain.community.comment.domain.CommentLike;

public interface RetrieveCommentLikePort {

    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, String memberId);
}
