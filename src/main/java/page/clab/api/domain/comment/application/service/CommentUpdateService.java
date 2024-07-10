package page.clab.api.domain.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.comment.application.port.in.UpdateCommentUseCase;
import page.clab.api.domain.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class CommentUpdateService implements UpdateCommentUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final RegisterCommentPort registerCommentPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional
    @Override
    public Long updateComment(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException {
        Comment comment = retrieveCommentPort.findByIdOrThrow(commentId);
        comment.validateAccessPermission(retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo());
        comment.update(requestDto);
        registerCommentPort.save(comment);
        return comment.getBoardId();
    }
}
