package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.CommentLikeToggleUseCase;
import page.clab.api.domain.comment.application.port.out.LoadCommentLikeByCommentIdAndMemberIdPort;
import page.clab.api.domain.comment.application.port.out.LoadCommentPort;
import page.clab.api.domain.comment.application.port.out.RegisterCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RemoveCommentLikePort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.domain.CommentLike;
import page.clab.api.domain.member.application.MemberLookupUseCase;

@Service
@RequiredArgsConstructor
public class CommentLikeToggleService implements CommentLikeToggleUseCase {

    private final LoadCommentPort loadCommentPort;
    private final LoadCommentLikeByCommentIdAndMemberIdPort loadCommentLikeByCommentIdAndMemberIdPort;
    private final RegisterCommentLikePort registerCommentLikePort;
    private final RemoveCommentLikePort removeCommentLikePort;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional
    @Override
    public Long toggle(Long commentId) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Comment comment = loadCommentPort.findByIdOrThrow(commentId);
        return loadCommentLikeByCommentIdAndMemberIdPort.findByCommentIdAndMemberId(comment.getId(), currentMemberId)
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
