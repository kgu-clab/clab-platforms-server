package page.clab.api.domain.community.board.application.service;

import com.drew.lang.annotations.NotNull;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveHotBoardsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveHotBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotBoardRetrievalService implements RetrieveHotBoardsUseCase {

    private final RetrieveHotBoardPort retrieveHotBoardPort;
    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final BoardDtoMapper mapper;

    @Transactional
    @Override
    public List<BoardListResponseDto> retrieveHotBoards() {
        List<String> hotBoardIds = retrieveHotBoardPort.findAll();

        return hotBoardIds.stream()
                .map(hotBoardId -> retrieveBoardPort.getById(Long.parseLong(hotBoardId)))
                .map(board -> mapToBoardListResponseDto(board, getMemberDetailedInfoByBoard(board)))
                .toList();
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());

        return mapper.toListDto(board, memberInfo, commentCount);
    }
}
