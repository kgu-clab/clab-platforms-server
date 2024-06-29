package page.clab.api.domain.board.application;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardEmojiRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardEmojiCountResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FetchBoardDetailsServiceImpl implements FetchBoardDetailsService {

    private final MemberLookupService memberLookupService;
    private final BoardLookupService boardLookupService;
    private final BoardEmojiRepository boardEmojiRepository;

    @Transactional
    @Override
    public BoardDetailsResponseDto fetchBoardDetails(Long boardId) {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Board board = boardLookupService.getBoardByIdOrThrow(boardId);
        boolean isOwner = board.isOwner(currentMemberInfo.getMemberId());
        List<BoardEmojiCountResponseDto> emojiInfos = getBoardEmojiCountResponseDtoList(boardId, currentMemberInfo.getMemberId());
        return BoardDetailsResponseDto.toDto(board, currentMemberInfo, isOwner, emojiInfos);
    }

    @Transactional(readOnly = true)
    public List<BoardEmojiCountResponseDto> getBoardEmojiCountResponseDtoList(Long boardId, String memberId) {
        List<Tuple> results = boardEmojiRepository.findEmojiClickCountsByBoardId(boardId, memberId);
        return results.stream()
                .map(result -> new BoardEmojiCountResponseDto(
                        result.get("emoji", String.class),
                        result.get("count", Long.class),
                        result.get("isClicked", Boolean.class)))
                .collect(Collectors.toList());
    }
}
