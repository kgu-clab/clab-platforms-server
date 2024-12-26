package page.clab.api.domain.community.comment.adapter.out.persistence;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentJpaEntity, Long> {

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.boardId = ?1 AND c.parent.id IS NULL")
    Page<CommentJpaEntity> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.writerId = ?1 AND c.isDeleted = false")
    Page<CommentJpaEntity> findAllByWriterId(String memberId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CommentJpaEntity c WHERE c.boardId = ?1 AND c.isDeleted = false")
    Long countByBoardId(Long boardId);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.isDeleted = true AND c.boardId = ?1")
    Page<CommentJpaEntity> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.boardId = ?1 AND c.isDeleted = false")
    List<CommentJpaEntity> findByBoardId(Long boardId);
}
