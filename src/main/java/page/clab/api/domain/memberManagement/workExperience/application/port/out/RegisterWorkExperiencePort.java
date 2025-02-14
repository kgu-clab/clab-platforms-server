package page.clab.api.domain.memberManagement.workExperience.application.port.out;

import java.util.List;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

public interface RegisterWorkExperiencePort {

    WorkExperience save(WorkExperience workExperience);

    void saveAll(List<WorkExperience> workExperiences);
}
