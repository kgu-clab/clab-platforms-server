package page.clab.api.domain.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.ToggleCommentLikeUseCase;
import page.clab.api.domain.comment.application.port.out.RegisterCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RemoveCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.domain.CommentLike;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class CommentLikeToggleService implements ToggleCommentLikeUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final RetrieveCommentLikePort retrieveCommentLikePort;
    private final RegisterCommentLikePort registerCommentLikePort;
    private final RemoveCommentLikePort removeCommentLikePort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;

    @Transactional
    @Override
    public Long toggle(Long commentId) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Comment comment = retrieveCommentPort.findByIdOrThrow(commentId);
        return retrieveCommentLikePort.findByCommentIdAndMemberId(comment.getId(), currentMemberId)
                .map(commentLike -> {
                    removeCommentLikePort.delete(commentLike);
                    comment.decrementLikes();
                    return comment.getLikes();
                })
                .orElseGet(() -> {
                    CommentLike newLike = CommentLike.create(currentMemberId, comment.getId());
                    registerCommentLikePort.save(newLike);
                    comment.incrementLikes();
                    return comment.getLikes();
                });

    }
}
