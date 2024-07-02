package page.clab.api.domain.workExperience.application.port.out;

import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

public interface UpdateWorkExperiencePort {
    Optional<WorkExperience> findById(Long id);
    WorkExperience save(WorkExperience workExperience);
    default WorkExperience findWorkExperienceByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("[WorkExperience] id: " + id + "에 해당하는 경력사항이 존재하지 않습니다."));
    }
}