package page.clab.api.domain.workExperience.application.port.out;

import page.clab.api.domain.workExperience.domain.WorkExperience;

import java.util.Optional;

public interface LoadWorkExperiencePort {
    Optional<WorkExperience> findById(Long id);
    WorkExperience findWorkExperienceByIdOrThrow(Long id);
}
