package page.clab.api.domain.comment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.comment.application.CommentLookupUseCase;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CommentLookupService implements CommentLookupUseCase {

    private final CommentRepository commentRepository;

    @Override
    public Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("[Comment] id: " + commentId + "에 해당하는 댓글을 찾을 수 없습니다."));
    }
}
