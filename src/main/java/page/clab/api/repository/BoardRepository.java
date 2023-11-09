package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);
    List<Board> findAllByWriter(Member writer);

    List<Board> findAllByCategory(String category);
}
