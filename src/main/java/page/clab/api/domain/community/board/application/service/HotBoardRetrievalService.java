package page.clab.api.domain.community.board.application.service;

import com.drew.lang.annotations.NotNull;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveHotBoardsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveHotBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class HotBoardRetrievalService implements RetrieveHotBoardsUseCase {

    private final RetrieveHotBoardPort retrieveHotBoardPort;
    private final RetrieveBoardPort retrieveBoardPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final BoardHashtagRetrieveService boardHashtagRetrieveService;
    private final HotBoardRegisterService hotBoardRegisterService;
    private final Map<String, HotBoardSelectionStrategy> strategyMap;
    private final BoardDtoMapper mapper;

    @Transactional
    @Override
    public List<BoardListResponseDto> retrieveHotBoards(String strategyName) {
        String validatedStrategyName = validateStrategyName(strategyName);
        List<String> hotBoardIds = retrieveHotBoardPort.findByHotBoardStrategy(validatedStrategyName);
        if (hotBoardIds.isEmpty()) {
            hotBoardIds = hotBoardRegisterService.registerHotBoards(validatedStrategyName);
        }

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
        List<BoardHashtagResponseDto> boardHashtagInfos = boardHashtagRetrieveService.getBoardHashtagInfoByBoardId(board.getId());
        return mapper.toListDto(board, memberInfo, commentCount, boardHashtagInfos);
    }

    private String validateStrategyName(String strategyName) {
        if (strategyName == null || !strategyMap.containsKey(strategyName)) {
            strategyName = HotBoardSelectionStrategies.DEFAULT;
        }
        return strategyName;
    }
}
