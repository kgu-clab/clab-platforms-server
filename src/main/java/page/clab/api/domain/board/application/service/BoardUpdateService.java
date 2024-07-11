package page.clab.api.domain.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.board.application.port.in.UpdateBoardUseCase;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardUpdateService implements UpdateBoardUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RegisterBoardPort registerBoardPort;

    @Transactional
    @Override
    public String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.findByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        return registerBoardPort.save(board).getCategory().getKey();
    }
}
