package page.clab.api.domain.community.board.application.port.out;

import jakarta.persistence.Tuple;
import page.clab.api.domain.community.board.domain.BoardEmoji;

import java.util.List;
import java.util.Optional;

public interface RetrieveBoardEmojiPort {

    List<Tuple> findEmojiClickCountsByBoardId(Long boardId, String memberId);

    Optional<BoardEmoji> findByBoardIdAndMemberIdAndEmoji(Long boardId, String memberId, String emoji);
}
