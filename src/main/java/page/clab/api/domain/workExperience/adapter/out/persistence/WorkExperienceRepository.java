package page.clab.api.domain.workExperience.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.workExperience.domain.WorkExperience;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    Page<WorkExperience> findByMemberId(String memberId, Pageable pageable);

    List<WorkExperience> findByMemberId(String memberId);

    @Query(value = "SELECT w.* FROM work_experience w WHERE w.is_deleted = true", nativeQuery = true)
    Page<WorkExperience> findAllByIsDeletedTrue(Pageable pageable);
}
