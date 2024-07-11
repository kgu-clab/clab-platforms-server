package page.clab.api.domain.application.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, ApplicationId>, ApplicationRepositoryCustom {
    List<ApplicationJpaEntity> findByRecruitmentIdAndIsPass(Long recruitmentId, Boolean isPass);

    Optional<ApplicationJpaEntity> findByRecruitmentIdAndStudentId(Long recruitmentId, String studentId);

    @Query(value = "SELECT a.* FROM application a WHERE a.is_deleted = true", nativeQuery = true)
    Page<ApplicationJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
