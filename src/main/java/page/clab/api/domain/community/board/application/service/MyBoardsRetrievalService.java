package page.clab.api.domain.community.board.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardMyResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveMyBoardsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardHashtag;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardHashtagUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyBoardsRetrievalService implements RetrieveMyBoardsUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveBoardHashtagUseCase externalRetrieveBoardHashtagUseCase;
    private final BoardDtoMapper mapper;

    @Transactional
    @Override
    public PagedResponseDto<BoardMyResponseDto> retrieveMyBoards(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Page<Board> boards = retrieveBoardPort.findAllByMemberId(currentMemberInfo.getMemberId(), pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            List<BoardHashtagResponseDto> boardHashtagInfos = externalRetrieveBoardHashtagUseCase.getAllByBoardId(board.getId());
            return mapper.toDto(board, currentMemberInfo, boardHashtagInfos);
        }));
    }
}
