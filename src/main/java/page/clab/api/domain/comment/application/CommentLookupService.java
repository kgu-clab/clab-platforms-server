package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.comment.application.port.in.CommentLookupUseCase;
import page.clab.api.domain.comment.application.port.out.LoadCommentPort;
import page.clab.api.domain.comment.domain.Comment;

@Service
@RequiredArgsConstructor
public class CommentLookupService implements CommentLookupUseCase {

    private final LoadCommentPort loadCommentPort;

    @Override
    public Comment getCommentByIdOrThrow(Long commentId) {
        return loadCommentPort.findByIdOrThrow(commentId);
    }
}
