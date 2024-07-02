package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.CommentRemoveUseCase;
import page.clab.api.domain.comment.application.port.out.LoadCommentPort;
import page.clab.api.domain.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class CommentRemoveService implements CommentRemoveUseCase {

    private final LoadCommentPort loadCommentPort;
    private final RegisterCommentPort registerCommentPort;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional
    @Override
    public Long remove(Long commentId) throws PermissionDeniedException {
        Comment comment = loadCommentPort.findByIdOrThrow(commentId);
        comment.validateAccessPermission(memberLookupUseCase.getCurrentMemberDetailedInfo());
        comment.delete();
        registerCommentPort.save(comment);
        return comment.getBoard().getId();
    }
}
