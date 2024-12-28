package page.clab.api.domain.community.board.adapter.out.persistence;

import jakarta.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardEmojiRepository extends JpaRepository<BoardEmojiJpaEntity, Long> {

    @Query(value = "SELECT b.* FROM board_emoji b WHERE b.board_id = :boardId AND b.member_id = :memberId AND b.emoji = :emoji", nativeQuery = true)
    Optional<BoardEmojiJpaEntity> findByBoardIdAndMemberIdAndEmoji(@Param("boardId") Long boardId,
        @Param("memberId") String memberId, @Param("emoji") String emoji);

    @Query("SELECT b.emoji as emoji, COUNT(b) as count, " +
        "CASE WHEN SUM(CASE WHEN b.memberId = :memberId THEN 1 ELSE 0 END) > 0 THEN true ELSE false END as isClicked " +
        "FROM BoardEmojiJpaEntity b " +
        "WHERE b.boardId = :boardId " +
        "GROUP BY b.emoji")
    List<Tuple> findEmojiClickCountsByBoardId(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM board_emoji b WHERE b.is_deleted = true AND b.deleted_at < :cutoffDate", nativeQuery = true)
    void deleteOldSoftDeletedRecords(LocalDateTime cutoffDate);
}
