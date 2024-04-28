package page.clab.api.domain.comment.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoardIdAndParentIsNullOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    Page<Comment> findAllByWriterOrderByCreatedAtDesc(Member member, Pageable pageable);

    Long countByBoard(Board board);

}
