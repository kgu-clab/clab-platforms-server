package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveHotBoardsUseCase;
import page.clab.api.domain.community.board.domain.HotBoardStrategyType;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HotBoardsRetrievalService implements RetrieveHotBoardsUseCase {

    private final Map<String, HotBoardsStrategy> strategies;

    @Override
    public List<BoardListResponseDto> retrieveHotBoards(int size, HotBoardStrategyType type) {
        HotBoardsStrategy hotBoardsStrategy = strategies.get(type.getKey());
        return hotBoardsStrategy.retrieveHotBoards(size);
    }
}
