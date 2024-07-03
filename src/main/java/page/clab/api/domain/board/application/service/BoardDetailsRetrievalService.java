package page.clab.api.domain.board.application.service;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.RetrieveBoardDetailsUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardEmojiCountResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardDetailsRetrievalService implements RetrieveBoardDetailsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveBoardEmojiPort retrieveBoardEmojiPort;

    @Transactional
    @Override
    public BoardDetailsResponseDto retrieve(Long boardId) {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.findByIdOrThrow(boardId);
        boolean isOwner = board.isOwner(currentMemberInfo.getMemberId());
        List<BoardEmojiCountResponseDto> emojiInfos = getBoardEmojiCountResponseDtoList(boardId, currentMemberInfo.getMemberId());
        return BoardDetailsResponseDto.toDto(board, currentMemberInfo, isOwner, emojiInfos);
    }

    @Transactional(readOnly = true)
    public List<BoardEmojiCountResponseDto> getBoardEmojiCountResponseDtoList(Long boardId, String memberId) {
        List<Tuple> results = retrieveBoardEmojiPort.findEmojiClickCountsByBoardId(boardId, memberId);
        return results.stream()
                .map(result -> new BoardEmojiCountResponseDto(
                        result.get("emoji", String.class),
                        result.get("count", Long.class),
                        result.get("isClicked", Boolean.class)))
                .collect(Collectors.toList());
    }
}
