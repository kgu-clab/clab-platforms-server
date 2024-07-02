package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardLookupUseCase;
import page.clab.api.domain.board.application.BoardUpdateUseCase;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BoardUpdateService implements BoardUpdateUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final BoardLookupUseCase boardLookupUseCase;
    private final ValidationService validationService;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public String update(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        Board board = boardLookupUseCase.getBoardByIdOrThrow(boardId);
        board.validateAccessPermission(currentMemberInfo);
        board.update(requestDto);
        validationService.checkValid(board);
        return boardRepository.save(board).getCategory().getKey();
    }
}
