package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.UpdateCommentUseCase;
import page.clab.api.domain.comment.application.port.out.LoadCommentPort;
import page.clab.api.domain.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CommentUpdateService implements UpdateCommentUseCase {

    private final LoadCommentPort loadCommentPort;
    private final RegisterCommentPort registerCommentPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException {
        Comment comment = loadCommentPort.findByIdOrThrow(commentId);
        comment.validateAccessPermission(retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo());
        comment.update(requestDto);
        validationService.checkValid(comment);
        registerCommentPort.save(comment);
        return comment.getBoard().getId();
    }
}
