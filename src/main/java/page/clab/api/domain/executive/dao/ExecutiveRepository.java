package page.clab.api.domain.executive.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.domain.executive.domain.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Long> {

    @Query("SELECT e FROM Executive e ORDER BY e.year DESC, e.position ASC")
    Page<Executive> findAllByOrderByYearDescPositionAsc(Pageable pageable);

    Page<Executive> findAllByYearOrderByPositionAsc(String year, Pageable pageable);

}
