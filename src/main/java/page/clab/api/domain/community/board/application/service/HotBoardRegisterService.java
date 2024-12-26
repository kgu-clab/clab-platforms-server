package page.clab.api.domain.community.board.application.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.port.out.RegisterHotBoardPort;
import page.clab.api.domain.community.board.application.port.out.RemoveHotBoardPort;
import page.clab.api.domain.community.board.domain.Board;

@Service
@RequiredArgsConstructor
public class HotBoardRegisterService {

    private final Map<String, HotBoardSelectionStrategy> strategyMap;
    private final RegisterHotBoardPort registerHotBoardPort;
    private final RemoveHotBoardPort removeHotBoardPort;

    /**
     * 주어진 전략을 기반으로 인기 게시글을 등록합니다.
     * <p>
     * 지정된 전략을 기반으로 인기 게시글을 선정하고, 이를 Redis에 저장합니다.
     *
     * @param strategyName 인기 게시글 선정 전략 이름
     * @return 인기 게시글 ID 리스트
     */
    @Transactional
    public List<String> registerHotBoards(String strategyName) {
        HotBoardSelectionStrategy strategy = strategyMap.get(strategyName);
        List<String> hotBoardIds = getHotBoardIds(strategy);
        hotBoardIds.forEach(id -> registerHotBoardPort.save(id, strategyName));
        return hotBoardIds;
    }

    /**
     * 기본 전략을 이용하여 인기 게시글을 등록합니다.
     * <p>
     * 매주 월요일 자정에 실행되도록 스케줄링되어 있습니다. 먼저 기존 인기 게시글을 초기화한 뒤,
     * 기본 전략을 이용하여 선정된 인기 게시글을 Redis에 저장합니다.
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 00:00 실행
    public void registerDefaultHotBoards() {
        HotBoardSelectionStrategy strategy = strategyMap.get(HotBoardSelectionStrategies.DEFAULT);

        removeHotBoardPort.clearHotBoard(); // 저장된 지난 모든 인기 게시글 초기화

        List<String> hotBoardIds = getHotBoardIds(strategy);
        hotBoardIds.forEach(id ->
                registerHotBoardPort.save(id, HotBoardSelectionStrategies.DEFAULT));
    }

    private static List<String> getHotBoardIds(HotBoardSelectionStrategy strategy) {
        return strategy.getHotBoards().stream()
                .map(Board::getId)
                .map(String::valueOf)
                .toList();
    }
}
