package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class DeleteBoardServiceImpl implements DeleteBoardService {

    private final MemberLookupService memberLookupService;
    private final BoardLookupService boardLookupService;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public String deleteBoard(Long boardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Board board = boardLookupService.getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.delete();
        boardRepository.save(board);
        return board.getCategory().getKey();
    }
}
