package page.clab.api.domain.comment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.comment.application.port.out.RegisterCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RemoveCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentLikePort;
import page.clab.api.domain.comment.domain.CommentLike;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentLikePersistenceAdapter implements
        RegisterCommentLikePort,
        RemoveCommentLikePort,
        RetrieveCommentLikePort {

    private final CommentLikeRepository commentLikeRepository;

    @Override
    public CommentLike save(CommentLike commentLike) {
        return commentLikeRepository.save(commentLike);
    }

    @Override
    public void delete(CommentLike commentLike) {
        commentLikeRepository.delete(commentLike);
    }

    @Override
    public Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, String memberId) {
        return commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId);
    }
}
