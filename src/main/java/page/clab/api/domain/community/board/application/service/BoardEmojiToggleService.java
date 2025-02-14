package page.clab.api.domain.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.mapper.BoardEmojiDtoMapper;
import page.clab.api.domain.community.board.application.dto.response.BoardEmojiToggleResponseDto;
import page.clab.api.domain.community.board.application.port.in.ToggleBoardEmojiUseCase;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardEmoji;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.util.EmojiUtils;

@Service
@RequiredArgsConstructor
public class BoardEmojiToggleService implements ToggleBoardEmojiUseCase {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveBoardEmojiPort retrieveBoardEmojiPort;
    private final RegisterBoardEmojiPort registerBoardEmojiPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final BoardEmojiDtoMapper mapper;

    /**
     * 게시글의 이모지 상태를 토글합니다.
     *
     * <p>이모지가 유효한지 검증한 후, 해당 이모지가 이미 존재하면 삭제 상태를 토글합니다.
     * 이모지가 없으면 새로 생성하여 저장합니다.</p>
     *
     * @param boardId 이모지를 추가하거나 토글할 게시글의 ID
     * @param emoji   추가할 이모지
     * @return 게시글의 카테고리 키
     * @throws InvalidEmojiException 지원하지 않는 이모지를 사용할 경우 예외 발생
     */
    @Transactional
    @Override
    public BoardEmojiToggleResponseDto toggleEmojiStatus(Long boardId, String emoji) {
        if (!EmojiUtils.isEmoji(emoji)) {
            throw new InvalidEmojiException("지원하지 않는 이모지입니다.");
        }
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        String memberId = currentMemberInfo.getMemberId();
        Board board = retrieveBoardPort.getById(boardId);
        BoardEmoji boardEmoji = retrieveBoardEmojiPort.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
            .map(existingEmoji -> {
                existingEmoji.toggleIsDeletedStatus();
                return existingEmoji;
            })
            .orElseGet(() -> BoardEmoji.create(memberId, boardId, emoji));
        registerBoardEmojiPort.save(boardEmoji);
        return mapper.toDto(boardEmoji, board.getCategory().getKey());
    }
}
