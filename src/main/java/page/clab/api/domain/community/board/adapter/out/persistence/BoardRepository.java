package page.clab.api.domain.community.board.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.community.board.domain.BoardCategory;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardJpaEntity, Long> {

    Page<BoardJpaEntity> findAllByMemberIdAndIsDeletedFalse(String memberId, Pageable pageable);

    Page<BoardJpaEntity> findAllByCategory(BoardCategory category, Pageable pageable);

    @Query(value = "SELECT b.* FROM board b WHERE b.is_deleted = true", nativeQuery = true)
    Page<BoardJpaEntity> findAllByIsDeletedTrue(Pageable pageable);

    List<BoardJpaEntity> findByMemberId(String memberId);
}
