package page.clab.api.domain.community.board.application.port.out;

import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import page.clab.api.domain.community.board.domain.BoardEmoji;

public interface RetrieveBoardEmojiPort {

    List<Tuple> findEmojiClickCountsByBoardId(Long boardId, String memberId);

    Optional<BoardEmoji> findByBoardIdAndMemberIdAndEmoji(Long boardId, String memberId, String emoji);
}
