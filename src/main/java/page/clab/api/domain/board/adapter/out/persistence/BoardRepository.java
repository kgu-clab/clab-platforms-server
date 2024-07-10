package page.clab.api.domain.board.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByMemberId(String memberId, Pageable pageable);

    Page<Board> findAllByCategory(BoardCategory category, Pageable pageable);

    @Query(value = "SELECT b.* FROM board b WHERE b.is_deleted = true", nativeQuery = true)
    Page<Board> findAllByIsDeletedTrue(Pageable pageable);

    List<Board> findByMemberId(String memberId);
}
