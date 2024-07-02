package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardLookupUseCase;
import page.clab.api.domain.board.application.BoardRemoveUseCase;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BoardRemoveService implements BoardRemoveUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final BoardLookupUseCase boardLookupUseCase;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public String remove(Long boardId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        Board board = boardLookupUseCase.getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.delete();
        boardRepository.save(board);
        return board.getCategory().getKey();
    }
}
