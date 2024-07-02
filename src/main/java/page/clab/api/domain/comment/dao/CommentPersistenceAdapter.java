package page.clab.api.domain.comment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.comment.application.port.out.LoadCommentLikeByCommentIdAndMemberIdPort;
import page.clab.api.domain.comment.application.port.out.LoadCommentPort;
import page.clab.api.domain.comment.application.port.out.RegisterCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.comment.application.port.out.RemoveCommentLikePort;
import page.clab.api.domain.comment.application.port.out.RemoveCommentPort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentsByBoardIdAndParentIsNullPort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentsByWriterIdPort;
import page.clab.api.domain.comment.application.port.out.RetrieveDeletedCommentsPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.domain.CommentLike;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements
        RegisterCommentPort,
        RemoveCommentPort,
        LoadCommentPort,
        RetrieveDeletedCommentsPort,
        RetrieveCommentsByBoardIdAndParentIsNullPort,
        RetrieveCommentsByWriterIdPort,
        RegisterCommentLikePort,
        RemoveCommentLikePort,
        LoadCommentLikeByCommentIdAndMemberIdPort {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Comment findByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("[Comment] id: " + commentId + "에 해당하는 댓글이 존재하지 않습니다."));
    }

    @Override
    public Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
    }

    @Override
    public Page<Comment> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNull(boardId, pageable);
    }

    @Override
    public Page<Comment> findAllByWriterId(String memberId, Pageable pageable) {
        return commentRepository.findAllByWriterId(memberId, pageable);
    }

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
