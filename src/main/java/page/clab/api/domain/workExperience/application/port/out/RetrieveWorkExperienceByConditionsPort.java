package page.clab.api.domain.workExperience.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.workExperience.domain.WorkExperience;

public interface RetrieveWorkExperienceByConditionsPort {
    Page<WorkExperience> findByConditions(String memberId, Pageable pageable);
}
