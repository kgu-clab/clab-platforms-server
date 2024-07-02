package page.clab.api.domain.workExperience.application.port.out;

import page.clab.api.domain.workExperience.domain.WorkExperience;

import java.util.Optional;

public interface UpdateWorkExperiencePort {
    Optional<WorkExperience> findById(Long id);
    WorkExperience save(WorkExperience workExperience);
    WorkExperience findWorkExperienceByIdOrThrow(Long id);
}