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
import page.clab.api.external.community.comment.application.port.ExternalRegisterCommentUseCase;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class CommentLikeToggleService implements ToggleCommentLikeUseCase {

    private final RetrieveCommentLikePort retrieveCommentLikePort;
    private final RegisterCommentLikePort registerCommentLikePort;
    private final RemoveCommentLikePort removeCommentLikePort;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRegisterCommentUseCase externalRegisterCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    /**
     * 댓글의 좋아요 상태를 토글합니다.
     *
     * <p>현재 사용자가 해당 댓글에 좋아요를 누른 상태인 경우 좋아요를 취소하고,
     * 그렇지 않은 경우 새롭게 좋아요를 추가합니다.
     * 댓글의 좋아요 수를 업데이트한 후 해당 수를 반환합니다.</p>
     *
     * @param commentId 좋아요를 토글할 댓글의 ID
     * @return 업데이트된 댓글의 좋아요 수
     */
    @Transactional
    @Override
    public Long toggleLikeStatus(Long commentId) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        Comment comment = externalRetrieveCommentUseCase.getById(commentId);
        return retrieveCommentLikePort.findByCommentIdAndMemberId(comment.getId(), currentMemberId)
                .map(commentLike -> {
                    removeCommentLikePort.delete(commentLike);
                    comment.decrementLikes();
                    return externalRegisterCommentUseCase.save(comment).getLikes();
                })
                .orElseGet(() -> {
                    CommentLike newLike = CommentLike.create(currentMemberId, comment.getId());
                    registerCommentLikePort.save(newLike);
                    comment.incrementLikes();
                    return externalRegisterCommentUseCase.save(comment).getLikes();
                });
    }
}
