package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.type.entity.Executives;

public interface ExecutivesRepository extends JpaRepository<Executives, Long> {

    @Query("SELECT e FROM Executives e ORDER BY e.year DESC, e.position ASC")
    Page<Executives> findAllByOrderByYearDescPositionAsc(Pageable pageable);

    Page<Executives> findAllByYearOrderByPositionAsc(String year, Pageable pageable);

}
