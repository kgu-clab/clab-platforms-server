package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.HotBoardSelectionStrategies;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service(HotBoardSelectionStrategies.DEFAULT)
@RequiredArgsConstructor
public class DefaultHotBoardSelectionStrategy implements HotBoardSelectionStrategy {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveBoardEmojiPort retrieveBoardEmojiPort;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;

    @Transactional
    @Override
    public List<Board> getHotBoards() {
        // 만약 게시글의 총 개수가 5개보다 적다면 모든 게시글 반환
        List<Board> allBoards = retrieveBoardPort.findAll();
        if (allBoards.size() < 5) {
            return sortBoardsByReactionAndDateWithLimit(allBoards.size(), allBoards);
        }

        List<Board> hotBoards = getHotBoardsForWeek(1, 5);

        int weeksAgo = 2;
        // 필요한 수량을 확보할 때까지 반복해서 이전 주로 이동하여 인기 게시글 보충
        while (hotBoards.size() < 5) {
            List<Board> additionalBoards = getLatestHotBoardForWeek(weeksAgo++, 5 - hotBoards.size());
            if (additionalBoards != null && !additionalBoards.isEmpty()) {
                hotBoards.addAll(additionalBoards);
            }
        }

        return hotBoards;
    }

    private List<Board> getHotBoardsForWeek(int weeksAgo, int size) {
        LocalDateTime startOfWeek = LocalDate.now().minusWeeks(weeksAgo).with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now().minusWeeks(weeksAgo).with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        List<Board> boardsForWeek = retrieveBoardPort.findAllWithinDateRange(startOfWeek, endOfWeek);

        return sortBoardsByReactionAndDateWithLimit(size, boardsForWeek);
    }

    private List<Board> sortBoardsByReactionAndDateWithLimit(int size, List<Board> boardsForWeek) {
        if (boardsForWeek == null) {
            return null;
        }

        return boardsForWeek.stream()
                .sorted(Comparator
                        .comparingInt(this::getTotalReactionCount).reversed()
                        .thenComparing(Board::getCreatedAt, Comparator.reverseOrder()))
                .limit(size)
                .collect(Collectors.toList());
    }

    private List<Board> getLatestHotBoardForWeek(int weeksAgo, int size) {

        List<Board> topHotBoardsForWeek = getHotBoardsForWeek(weeksAgo, size);

        return topHotBoardsForWeek.stream()
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .toList();
    }

    private int getTotalReactionCount(Board board) {
        Long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());
        int emojiCount = retrieveBoardEmojiPort.findEmojiClickCountsByBoardId(board.getId(), null).size();

        return commentCount.intValue() + emojiCount;
    }
}
