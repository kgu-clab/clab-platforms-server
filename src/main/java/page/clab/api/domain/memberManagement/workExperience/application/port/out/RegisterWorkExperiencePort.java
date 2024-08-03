package page.clab.api.domain.memberManagement.workExperience.application.port.out;

import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

import java.util.List;

public interface RegisterWorkExperiencePort {

    WorkExperience save(WorkExperience workExperience);

    void saveAll(List<WorkExperience> workExperiences);
}
