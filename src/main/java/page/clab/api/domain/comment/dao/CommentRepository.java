package page.clab.api.domain.comment.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable);

    Page<Comment> findAllByWriter(Member member, Pageable pageable);

    Long countByBoard(Board board);

    @Query(value = "SELECT c.* FROM comment c WHERE c.is_deleted = true AND c.board_id = ?", nativeQuery = true)
    Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);

}
