package page.clab.api.domain.memberManagement.workExperience.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.UpdateWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkExperiencePersistenceAdapter implements
        RegisterWorkExperiencePort,
        UpdateWorkExperiencePort,
        RetrieveWorkExperiencePort {

    private final WorkExperienceRepository repository;
    private final WorkExperienceMapper mapper;

    @Override
    public WorkExperience save(WorkExperience workExperience) {
        WorkExperienceJpaEntity entity = mapper.toJpaEntity(workExperience);
        WorkExperienceJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public void saveAll(List<WorkExperience> workExperiences) {
        List<WorkExperienceJpaEntity> entities = workExperiences.stream()
                .map(mapper::toJpaEntity)
                .collect(Collectors.toList());
        repository.saveAll(entities);
    }

    @Override
    public Page<WorkExperience> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<WorkExperience> findByMemberId(String memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Page<WorkExperience> findByMemberId(String memberId, Pageable pageable) {
        Page<WorkExperienceJpaEntity> workExperienceJpaEntities = repository.findByMemberId(memberId, pageable);
        return workExperienceJpaEntities
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<WorkExperience> findByConditions(String memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public WorkExperience update(WorkExperience workExperience) {
        WorkExperienceJpaEntity entity = mapper.toJpaEntity(workExperience);
        WorkExperienceJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public Optional<WorkExperience> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public WorkExperience findByIdOrThrow(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("[WorkExperience] id: " + id + "에 해당하는 경력사항이 존재하지 않습니다."));
    }
}
