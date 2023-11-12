package page.clab.api.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Comment;
import page.clab.api.type.entity.Member;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoardId(Long boardId);

    List<Comment> findAllByWriter(Member member);

}
