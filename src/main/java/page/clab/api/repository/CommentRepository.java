package page.clab.api.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Comment;
import page.clab.api.type.entity.Member;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoardIdOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    Page<Comment> findAllByWriterOrderByCreatedAtDesc(Member member, Pageable pageable);

}
