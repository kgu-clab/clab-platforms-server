package page.clab.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByMember(Member member);

    List<Board> findAllByCategory(String category);

}
