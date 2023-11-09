package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByWriter(Member writer);

    List<Board> findAllByCategory(String category);
}
