package page.clab.api.domain.workExperience.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.workExperience.domain.WorkExperience;

import java.util.Optional;

public interface RetrieveWorkExperiencePort {
    Optional<WorkExperience> findById(Long id);
    WorkExperience findByIdOrThrow(Long id);
    Page<WorkExperience> findAllByIsDeletedTrue(Pageable pageable);
    Page<WorkExperience> findByMemberId(String memberId, Pageable pageable);
    Page<WorkExperience> findByConditions(String memberId, Pageable pageable);
}
