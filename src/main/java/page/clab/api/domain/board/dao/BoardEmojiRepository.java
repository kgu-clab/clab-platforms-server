package page.clab.api.domain.board.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.BoardEmoji;

@Repository
public interface BoardEmojiRepository extends JpaRepository<BoardEmoji, Long> {

    @Query(value = "SELECT b.* FROM board_emoji b WHERE b.board_id = :boardId AND b.member_id = :memberId AND b.emoji_unicode = :emojiUnicode", nativeQuery = true)
    Optional<BoardEmoji> findByBoardIdAndMemberIdAndEmojiUniCode(Long boardId, String memberId, String emojiUnicode);

}
