package page.clab.api.domain.board.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.BoardEmoji;
import page.clab.api.domain.board.dto.response.BoardEmojiCountResponseDto;

@Repository
public interface BoardEmojiRepository extends JpaRepository<BoardEmoji, Long> {

    @Query(value = "SELECT b.* FROM board_emoji b WHERE b.board_id = :boardId AND b.member_id = :memberId AND b.emoji = :emoji", nativeQuery = true)
    Optional<BoardEmoji> findByBoardIdAndMemberIdAndEmoji(@Param("boardId") Long boardId, @Param("memberId") String memberId, @Param("emoji") String emoji);

    @Query("SELECT new page.clab.api.domain.board.dto.response.BoardEmojiCountResponseDto(b.emoji, COUNT(b), " +
            "CASE WHEN SUM(CASE WHEN b.memberId = :memberId THEN 1 ELSE 0 END) > 0 THEN true ELSE false END) " +
            "FROM BoardEmoji b " +
            "WHERE b.boardId = :boardId " +
            "GROUP BY b.emoji")
    List<BoardEmojiCountResponseDto> findEmojiClickCountsByBoardId(@Param("boardId") Long boardId, @Param("memberId") String memberId);

}
