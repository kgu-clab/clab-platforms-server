package page.clab.api.domain.memberManagement.workExperience.application.port.out;

import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

public interface UpdateWorkExperiencePort {
    WorkExperience update(WorkExperience workExperience);
}
