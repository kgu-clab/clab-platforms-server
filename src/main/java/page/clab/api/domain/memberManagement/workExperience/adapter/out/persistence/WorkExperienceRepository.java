package page.clab.api.domain.memberManagement.workExperience.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperienceJpaEntity, Long> {

    Page<WorkExperienceJpaEntity> findByMemberId(String memberId, Pageable pageable);

    List<WorkExperienceJpaEntity> findByMemberId(String memberId);

    @Query(value = "SELECT w.* FROM work_experience w WHERE w.is_deleted = true", nativeQuery = true)
    Page<WorkExperienceJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
