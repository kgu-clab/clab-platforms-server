package page.clab.api.domain.position.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.domain.position.domain.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT e FROM Position e ORDER BY e.year DESC, e.positionType ASC")
    Page<Position> findAllByOrderByYearDescPositionAsc(Pageable pageable);

    Page<Position> findAllByYearOrderByPositionAsc(String year, Pageable pageable);

}
