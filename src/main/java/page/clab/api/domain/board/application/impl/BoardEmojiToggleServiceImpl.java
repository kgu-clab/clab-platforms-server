package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardLookupService;
import page.clab.api.domain.board.application.BoardEmojiToggleService;
import page.clab.api.domain.board.dao.BoardEmojiRepository;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardEmoji;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.util.EmojiUtils;

@Service
@RequiredArgsConstructor
public class BoardEmojiToggleServiceImpl implements BoardEmojiToggleService {

    private final MemberLookupService memberLookupService;
    private final BoardLookupService boardLookupService;
    private final BoardRepository boardRepository;
    private final BoardEmojiRepository boardEmojiRepository;

    @Transactional
    @Override
    public String toggleEmojiStatus(Long boardId, String emoji) {
        if (!EmojiUtils.isEmoji(emoji)) {
            throw new InvalidEmojiException("지원하지 않는 이모지입니다.");
        }
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        String memberId = currentMemberInfo.getMemberId();
        Board board = boardLookupService.getBoardByIdOrThrow(boardId);
        BoardEmoji boardEmoji = boardEmojiRepository.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
                .map(existingEmoji -> {
                    existingEmoji.toggleIsDeletedStatus();
                    return existingEmoji;
                })
                .orElseGet(() -> BoardEmoji.create(memberId, boardId, emoji));
        boardEmojiRepository.save(boardEmoji);
        return board.getCategory().getKey();
    }
}
