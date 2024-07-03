package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.BoardEmojiToggleUseCase;
import page.clab.api.domain.board.application.port.out.LoadBoardEmojiPort;
import page.clab.api.domain.board.application.port.out.LoadBoardPort;
import page.clab.api.domain.board.application.port.out.RegisterBoardEmojiPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardEmoji;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.util.EmojiUtils;

@Service
@RequiredArgsConstructor
public class BoardEmojiToggleService implements BoardEmojiToggleUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final LoadBoardPort loadBoardPort;
    private final LoadBoardEmojiPort loadBoardEmojiPort;
    private final RegisterBoardEmojiPort registerBoardEmojiPort;

    @Transactional
    @Override
    public String toggleEmojiStatus(Long boardId, String emoji) {
        if (!EmojiUtils.isEmoji(emoji)) {
            throw new InvalidEmojiException("지원하지 않는 이모지입니다.");
        }
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        String memberId = currentMemberInfo.getMemberId();
        Board board = loadBoardPort.findByIdOrThrow(boardId);
        BoardEmoji boardEmoji = loadBoardEmojiPort.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
                .map(existingEmoji -> {
                    existingEmoji.toggleIsDeletedStatus();
                    return existingEmoji;
                })
                .orElseGet(() -> BoardEmoji.create(memberId, boardId, emoji));
        registerBoardEmojiPort.save(boardEmoji);
        return board.getCategory().getKey();
    }
}
