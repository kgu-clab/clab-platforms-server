package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class UpdateBoardServiceImpl implements UpdateBoardService {

    private final MemberLookupService memberLookupService;
    private final BoardLookupService boardLookupService;
    private final ValidationService validationService;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Board board = boardLookupService.getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        validationService.checkValid(board);
        return boardRepository.save(board).getCategory().getKey();
    }
}
