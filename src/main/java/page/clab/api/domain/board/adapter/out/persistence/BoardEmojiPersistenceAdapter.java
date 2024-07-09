package page.clab.api.domain.board.adapter.out.persistence;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.board.application.port.out.RegisterBoardEmojiPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.board.domain.BoardEmoji;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardEmojiPersistenceAdapter implements
        RetrieveBoardEmojiPort,
        RegisterBoardEmojiPort {

    private final BoardEmojiRepository boardEmojiRepository;

    @Override
    public List<Tuple> findEmojiClickCountsByBoardId(Long boardId, String memberId) {
        return boardEmojiRepository.findEmojiClickCountsByBoardId(boardId, memberId);
    }

    @Override
    public Optional<BoardEmoji> findByBoardIdAndMemberIdAndEmoji(Long boardId, String memberId, String emoji) {
        return boardEmojiRepository.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji);
    }

    @Override
    public BoardEmoji save(BoardEmoji boardEmoji) {
        return boardEmojiRepository.save(boardEmoji);
    }
}
