package page.clab.api.domain.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardInfoUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.comment.application.dto.request.CommentRequestDto;
import page.clab.api.domain.community.comment.application.port.in.RegisterCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class CommentRegisterService implements RegisterCommentUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final SendNotificationUseCase notificationService;
    private final RegisterCommentPort registerCommentPort;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveCommentPort retrieveCommentPort;
    private final RetrieveBoardInfoUseCase retrieveBoardInfoUseCase;

    @Transactional
    @Override
    public Long registerComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Comment comment = createAndStoreComment(parentId, boardId, requestDto);
        sendNotificationForNewComment(comment);
        return boardId;
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Board board = retrieveBoardPort.findByIdOrThrow(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = CommentRequestDto.toEntity(requestDto, board.getId(), currentMemberId, parent);
        if (parent != null) {
            parent.addChildComment(comment);
        }
        return registerCommentPort.save(comment);
    }

    private Comment findParentComment(Long parentId) {
        return parentId != null ? retrieveCommentPort.findById(parentId).orElse(null) : null;
    }

    private void sendNotificationForNewComment(Comment comment) {
        BoardCommentInfoDto boardInfo = retrieveBoardInfoUseCase.getBoardCommentInfoById(comment.getBoardId());
        String notificationMessage = String.format("[%s] 새로운 댓글이 등록되었습니다.", boardInfo.getTitle());
        notificationService.sendNotificationToMember(boardInfo.getMemberId(), notificationMessage);
    }
}
