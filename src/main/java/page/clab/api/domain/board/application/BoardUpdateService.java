package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.UpdateBoardUseCase;
import page.clab.api.domain.board.application.port.out.LoadBoardPort;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BoardUpdateService implements UpdateBoardUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final LoadBoardPort loadBoardPort;
    private final ValidationService validationService;
    private final RegisterBoardPort registerBoardPort;

    @Transactional
    @Override
    public String update(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Board board = loadBoardPort.findByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        validationService.checkValid(board);
        return registerBoardPort.save(board).getCategory().getKey();
    }
}
