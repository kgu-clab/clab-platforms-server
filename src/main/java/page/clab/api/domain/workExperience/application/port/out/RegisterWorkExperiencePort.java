package page.clab.api.domain.workExperience.application.port.out;

import page.clab.api.domain.workExperience.domain.WorkExperience;

public interface RegisterWorkExperiencePort {
    WorkExperience save(WorkExperience workExperience);
}
