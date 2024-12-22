package page.clab.api.domain.hiring.application.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, ApplicationId>,
    ApplicationRepositoryCustom {

    List<ApplicationJpaEntity> findByRecruitmentIdAndIsPass(Long recruitmentId, Boolean isPass);

    Optional<ApplicationJpaEntity> findByRecruitmentIdAndStudentId(Long recruitmentId, String studentId);
}
