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

    @Override
    public Recruitment save(Recruitment recruitment) {
        return repository.save(recruitment);
    }

    @Override
    public Recruitment update(Recruitment recruitment) {
        return repository.save(recruitment);
    }

    @Override
    public Optional<Recruitment> findById(Long recruitmentId) {
        return repository.findById(recruitmentId);
    }

    @Override
    public Recruitment findByIdOrThrow(Long recruitmentId) {
        return repository.findById(recruitmentId)
                .orElseThrow(() -> new NotFoundException("[Recruitment] id: " + recruitmentId + "에 해당하는 모집 공고가 존재하지 않습니다."));
    }

    @Override
    public List<Recruitment> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Recruitment> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public List<Recruitment> findTop5ByOrderByCreatedAtDesc() {
        return repository.findTop5ByOrderByCreatedAtDesc();
    }
}
