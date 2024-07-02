package page.clab.api.domain.comment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.CommentLikeToggleUseCase;
import page.clab.api.domain.comment.dao.CommentLikeRepository;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.domain.CommentLike;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CommentLikeToggleService implements CommentLikeToggleUseCase {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional
    @Override
    public Long toggle(Long commentId) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Comment comment = getCommentByIdOrThrow(commentId);
        return commentLikeRepository.findByCommentIdAndMemberId(comment.getId(), currentMemberId)
                .map(commentLike -> {
                            commentLikeRepository.delete(commentLike);
                            comment.decrementLikes();
                            return comment.getLikes();
                        })
                .orElseGet(() -> {
                    CommentLike newLike = CommentLike.create(currentMemberId, comment.getId());
                    commentLikeRepository.save(newLike);
                    comment.incrementLikes();
                    return comment.getLikes();
                });

    }

    private Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }
}
