package page.clab.api.domain.community.board.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.community.board.domain.BoardCategory;

@Repository
public interface BoardRepository extends JpaRepository<BoardJpaEntity, Long> {

    @Query("SELECT b FROM BoardJpaEntity b WHERE b.memberId = ?1 AND b.isDeleted = false")
    Page<BoardJpaEntity> findAllByMemberIdAndIsDeletedFalse(String memberId, Pageable pageable);

    @Query("SELECT b FROM BoardJpaEntity b WHERE b.createdAt BETWEEN :start AND :end AND b.isDeleted = false")
    List<BoardJpaEntity> findAllWithinDateRange(LocalDateTime start, LocalDateTime end);

    Page<BoardJpaEntity> findAllByCategory(BoardCategory category, Pageable pageable);

    @Query(value = "SELECT b.* FROM board b WHERE b.is_deleted = true", nativeQuery = true)
    Page<BoardJpaEntity> findAllByIsDeletedTrue(Pageable pageable);

    List<BoardJpaEntity> findByMemberId(String memberId);

    @Query(value = "SELECT * FROM board b WHERE b.id = :boardId", nativeQuery = true)
    Optional<BoardJpaEntity> findByIdRegardlessOfDeletion(@Param("boardId") Long boardId);
}
