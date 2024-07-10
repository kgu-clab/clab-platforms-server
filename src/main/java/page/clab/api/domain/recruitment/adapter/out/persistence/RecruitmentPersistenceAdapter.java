package page.clab.api.domain.recruitment.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.recruitment.application.port.out.RegisterRecruitmentPort;
import page.clab.api.domain.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecruitmentPersistenceAdapter implements
        RegisterRecruitmentPort,
        UpdateRecruitmentPort,
        RetrieveRecruitmentPort {

    private final RecruitmentRepository repository;
    private final RecruitmentMapper mapper;

    @Override
    public Recruitment save(Recruitment recruitment) {
        RecruitmentJpaEntity entity = mapper.toJpaEntity(recruitment);
        RecruitmentJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Recruitment update(Recruitment recruitment) {
        RecruitmentJpaEntity entity = mapper.toJpaEntity(recruitment);
        RecruitmentJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public Optional<Recruitment> findById(Long recruitmentId) {
        return repository.findById(recruitmentId)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Recruitment findByIdOrThrow(Long recruitmentId) {
        return repository.findById(recruitmentId)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("[Recruitment] id: " + recruitmentId + "에 해당하는 모집 공고가 존재하지 않습니다."));
    }

    @Override
    public List<Recruitment> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Page<Recruitment> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Recruitment> findTop5ByOrderByCreatedAtDesc() {
        return repository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(mapper::toDomainEntity)
                .toList();
    }
}
