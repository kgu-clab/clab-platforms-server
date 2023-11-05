package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Recruitment;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();

}
