package page.clab.api.domain.hiring.recruitment.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.hiring.recruitment.domain.RecruitmentStatus;

@Repository
public interface RecruitmentRepository extends JpaRepository<RecruitmentJpaEntity, Long> {

    List<RecruitmentJpaEntity> findTop5ByOrderByCreatedAtDesc();

    List<RecruitmentJpaEntity> findByEndDateBetween(LocalDateTime weekAgo, LocalDateTime now);

    List<RecruitmentJpaEntity> findByStatus(RecruitmentStatus status);
}
