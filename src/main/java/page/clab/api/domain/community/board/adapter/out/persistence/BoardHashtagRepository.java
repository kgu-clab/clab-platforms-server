package page.clab.api.domain.community.board.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardHashtagRepository extends JpaRepository<BoardHashtagJpaEntity, Long> {
}
