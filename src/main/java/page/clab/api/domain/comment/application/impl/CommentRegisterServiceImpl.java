package page.clab.api.domain.comment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardLookupService;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.application.CommentRegisterService;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CommentRegisterServiceImpl implements CommentRegisterService {

    private final BoardLookupService boardLookupService;
    private final MemberLookupService memberLookupService;
    private final NotificationSenderService notificationService;
    private final ValidationService validationService;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Long register(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Comment comment = createAndStoreComment(parentId, boardId, requestDto);
        sendNotificationForNewComment(comment);
        return boardId;
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Board board = boardLookupService.getBoardByIdOrThrow(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = CommentRequestDto.toEntity(requestDto, board, currentMemberId, parent);
        if (parent != null) {
            parent.addChildComment(comment);
        }
        validationService.checkValid(comment);
        return commentRepository.save(comment);
    }

    private Comment findParentComment(Long parentId) {
        return parentId != null ? commentRepository.findById(parentId).orElse(null) : null;
    }

    private void sendNotificationForNewComment(Comment comment) {
        Board board = comment.getBoard();
        String notificationMessage = String.format("[%s] 새로운 댓글이 등록되었습니다.", board.getTitle());
        notificationService.sendNotificationToMember(board.getMemberId(), notificationMessage);
    }
}
