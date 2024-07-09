package page.clab.api.domain.workExperience.application.port.out;

import page.clab.api.domain.workExperience.domain.WorkExperience;

public interface UpdateWorkExperiencePort {
    WorkExperience update(WorkExperience workExperience);
}
