package page.clab.api.domain.comment.adapter.out.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentJpaEntity, Long> {

    Page<CommentJpaEntity> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable);

    Page<CommentJpaEntity> findAllByWriterId(String memberId, Pageable pageable);

    Long countByBoardId(Long boardId);

    @Query(value = "SELECT c.* FROM comment c WHERE c.is_deleted = true AND c.board_id = ?", nativeQuery = true)
    Page<CommentJpaEntity> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);

    List<CommentJpaEntity> findByBoardId(Long boardId);
}
