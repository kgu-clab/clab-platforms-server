package page.clab.api.domain.workExperience.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RemoveWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.UpdateWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkExperiencePersistenceAdapter implements
        RegisterWorkExperiencePort,
        UpdateWorkExperiencePort,
        RetrieveWorkExperiencePort,
        RemoveWorkExperiencePort {

    private final WorkExperienceRepository repository;

    @Override
    public WorkExperience save(WorkExperience workExperience) {
        return repository.save(workExperience);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
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
    public Page<WorkExperience> findByConditions(String memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable);  // 조건에 맞게 구현 필요
    }

    @Override
    public WorkExperience update(WorkExperience workExperience) {
        return repository.save(workExperience);
    }

    @Override
    public Optional<WorkExperience> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public WorkExperience findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("[WorkExperience] id: " + id + "에 해당하는 경력사항이 존재하지 않습니다."));
    }
}
