package page.clab.api.domain.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.event.BoardDeletedEvent;
import page.clab.api.domain.board.application.port.in.RemoveBoardUseCase;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardRemoveService implements RemoveBoardUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RegisterBoardPort registerBoardPort;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String removeBoard(Long boardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.findByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.delete();
        registerBoardPort.save(board);
        eventPublisher.publishEvent(new BoardDeletedEvent(this, board.getId()));
        return board.getCategory().getKey();
    }
}
