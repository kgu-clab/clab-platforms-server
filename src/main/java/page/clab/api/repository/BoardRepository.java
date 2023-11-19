package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Member;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Board> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    Page<Board> findAllByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

}
