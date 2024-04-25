package page.clab.api.domain.recruitment.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();

    @Query(value = "SELECT r.* FROM recruitment r WHERE r.is_deleted = true", nativeQuery = true)
    Page<Recruitment> findAllByIsDeletedTrue(Pageable pageable);

}
