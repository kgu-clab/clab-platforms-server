package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.port.in.ToggleCommentLikeUseCase;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentLikePort;
import page.clab.api.domain.community.comment.application.port.out.RemoveCommentLikePort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentLikePort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.community.comment.domain.CommentLike;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class CommentLikeToggleService implements ToggleCommentLikeUseCase {

    private final RetrieveCommentLikePort retrieveCommentLikePort;
    private final RegisterCommentLikePort registerCommentLikePort;
    private final RemoveCommentLikePort removeCommentLikePort;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long toggleLikeStatus(Long commentId) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Comment comment = externalRetrieveCommentUseCase.findByIdOrThrow(commentId);
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
