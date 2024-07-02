package page.clab.api.domain.workExperience.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RemoveWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RetrieveDeletedWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RetrieveMyWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RetrieveWorkExperienceByConditionsPort;
import page.clab.api.domain.workExperience.application.port.out.UpdateWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkExperiencePersistenceAdapter implements
        RetrieveDeletedWorkExperiencePort,
        RetrieveMyWorkExperiencePort,
        RegisterWorkExperiencePort,
        RemoveWorkExperiencePort,
        RetrieveWorkExperienceByConditionsPort,
        UpdateWorkExperiencePort {

    private final WorkExperienceRepository repository;

    @Override
    public Optional<WorkExperience> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<WorkExperience> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<WorkExperience> findByMemberId(String memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable);
    }

    @Override
    public WorkExperience save(WorkExperience workExperience) {
        return repository.save(workExperience);
    }

    @Override
    public WorkExperience findWorkExperienceByIdOrThrow(Long id) {
        return findById(id).
                orElseThrow(() -> new NotFoundException("[WorkExperience] id: " + id + "에 해당하는 경력사항이 존재하지 않습니다."));
    }
}
