package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.port.out.RegisterHotBoardPort;
import page.clab.api.domain.community.board.application.port.out.RemoveHotBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.HotBoardSelectionStrategies;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HotBoardRegisterService {

    private final Map<String, HotBoardSelectionStrategy> strategies;
    private final RegisterHotBoardPort registerHotBoardPort;
    private final RemoveHotBoardPort removeHotBoardPort;

    @Transactional
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 00:00 실행
    public void registerHotBoards() {
        HotBoardSelectionStrategy strategy = strategies.get(HotBoardSelectionStrategies.DEFAULT);

        removeHotBoardPort.clearHotBoard(); // 저장된 지난 인기 게시글 초기화

        List<String> hotBoardIds = strategy.getHotBoards().stream()
                .map(Board::getId)
                .map(String::valueOf)
                .toList();

        hotBoardIds.forEach(registerHotBoardPort::save);
    }
}
