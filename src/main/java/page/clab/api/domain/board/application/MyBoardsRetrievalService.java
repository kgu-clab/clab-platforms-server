package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.MyBoardsRetrievalUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveMyBoardsPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyBoardsRetrievalService implements MyBoardsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveMyBoardsPort retrieveMyBoardsPort;

    @Transactional
    @Override
    public PagedResponseDto<BoardMyResponseDto> retrieve(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberBasicInfo();
        Page<Board> boards = retrieveMyBoardsPort.findAllByMemberId(currentMemberInfo.getMemberId(), pageable);
        return new PagedResponseDto<>(boards.map(board -> BoardMyResponseDto.toDto(board, currentMemberInfo)));
    }
}
