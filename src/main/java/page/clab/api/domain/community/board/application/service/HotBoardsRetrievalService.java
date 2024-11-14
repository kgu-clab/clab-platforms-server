package page.clab.api.domain.community.board.application.service;

import com.drew.lang.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveHotBoardsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotBoardsRetrievalService implements RetrieveHotBoardsUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveBoardEmojiPort retrieveBoardEmojiPort;
    private final ExternalRetrieveCommentUseCase externalRetrieveCommentUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final BoardDtoMapper mapper;

    @Transactional
    @Override
    public List<BoardListResponseDto> retrieveHotBoards(int size) {
        List<Board> hotBoards = getHotBoards(size);

        return hotBoards.stream()
                .map(board -> mapToBoardListResponseDto(board, getMemberDetailedInfoByBoard(board)))
                .toList();
    }

    private List<Board> getHotBoards(int size) {
        // 만약 게시글의 총 개수가 size보다 적다면 모든 게시글 반환
        List<Board> allBoards = retrieveBoardPort.findAll();
        if (allBoards.size() < size) {
            return sortAndLimit(allBoards.size(), allBoards);
        }

        List<Board> hotBoards = getHotBoardsForWeek(1, size);

        int weeksAgo = 2;
        // 필요한 수량을 확보할 때까지 반복해서 이전 주로 이동하여 Hot 게시글 보충
        while (hotBoards.size() < size) {
            List<Board> additionalBoards = getLatestHotBoardForWeek(weeksAgo++, size - hotBoards.size());
            if (additionalBoards != null && !additionalBoards.isEmpty()) {
                hotBoards.addAll(additionalBoards);
            }
        }
        return hotBoards;
    }

    private List<Board> getHotBoardsForWeek(int weeksAgo, int size) {
        LocalDateTime startDate = LocalDate.now().minusWeeks(weeksAgo).with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().minusWeeks(weeksAgo).with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        List<Board> boardsForWeek = retrieveBoardPort.findAllWithinDateRange(startDate, endDate);

        return sortAndLimit(size, boardsForWeek);
    }

    private List<Board> sortAndLimit(int size, List<Board> boardsForWeek) {
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

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Board board) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(board.getMemberId());
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = externalRetrieveCommentUseCase.countByBoardId(board.getId());

        return mapper.toListDto(board, memberInfo, commentCount);
    }
}
