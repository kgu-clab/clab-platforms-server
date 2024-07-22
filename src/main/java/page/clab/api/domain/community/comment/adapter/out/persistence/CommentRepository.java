package page.clab.api.domain.community.comment.adapter.out.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentJpaEntity, Long> {

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.boardId = ?1")
    Page<CommentJpaEntity> findAllByBoardId(Long boardId, Pageable pageable);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.writerId = ?1 AND c.isDeleted = false")
    Page<CommentJpaEntity> findAllByWriterId(String memberId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CommentJpaEntity c WHERE c.boardId = ?1 AND c.isDeleted = false")
    Long countByBoardId(Long boardId);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.isDeleted = true AND c.boardId = ?1")
    Page<CommentJpaEntity> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.boardId = ?1 AND c.isDeleted = false")
    List<CommentJpaEntity> findByBoardId(Long boardId);
}
