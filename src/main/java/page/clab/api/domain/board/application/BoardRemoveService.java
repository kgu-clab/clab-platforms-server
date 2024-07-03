package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.BoardRemoveUseCase;
import page.clab.api.domain.board.application.port.out.LoadBoardPort;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardRemoveService implements BoardRemoveUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final LoadBoardPort loadBoardPort;
    private final RegisterBoardPort registerBoardPort;

    @Transactional
    @Override
    public String remove(Long boardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Board board = loadBoardPort.findByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.delete();
        registerBoardPort.save(board);
        return board.getCategory().getKey();
    }
}
