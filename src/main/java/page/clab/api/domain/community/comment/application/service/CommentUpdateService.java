package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.community.comment.application.port.in.UpdateCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class CommentUpdateService implements UpdateCommentUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final RegisterCommentPort registerCommentPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long updateComment(Long commentId, CommentUpdateRequestDto requestDto) {
        Comment comment = retrieveCommentPort.getById(commentId);
        comment.validateAccessPermission(externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo());
        comment.update(requestDto);
        registerCommentPort.save(comment);
        return comment.getBoardId();
    }
}
