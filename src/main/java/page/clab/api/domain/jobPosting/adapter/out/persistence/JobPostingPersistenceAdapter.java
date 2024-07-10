package page.clab.api.domain.jobPosting.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.jobPosting.application.port.out.RegisterJobPostingPort;
import page.clab.api.domain.jobPosting.application.port.out.RemoveJobPostingPort;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobPostingPersistenceAdapter implements
        RegisterJobPostingPort,
        RetrieveJobPostingPort,
        RemoveJobPostingPort {

    private final JobPostingRepository repository;

    @Override
    public JobPosting save(JobPosting jobPosting) {
        JobPostingJpaEntity entity = JobPostingMapper.toJpaEntity(jobPosting);
        JobPostingJpaEntity savedEntity = repository.save(entity);
        return JobPostingMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<JobPosting> findById(Long jobPostingId) {
        return repository.findById(jobPostingId).map(JobPostingMapper::toDomain);
    }

    @Override
    public JobPosting findByIdOrThrow(Long jobPostingId) {
        return repository.findById(jobPostingId)
                .map(JobPostingMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[JobPosting] id: " + jobPostingId + "에 해당하는 채용 공고가 존재하지 않습니다."));
    }

    @Override
    public Optional<JobPosting> findByJobPostingUrl(String jobPostingUrl) {
        return repository.findByJobPostingUrl(jobPostingUrl).map(JobPostingMapper::toDomain);
    }

    @Override
    public Page<JobPosting> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable).map(JobPostingMapper::toDomain);
    }

    @Override
    public Page<JobPosting> findByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable) {
        return repository.findByConditions(title, companyName, careerLevel, employmentType, pageable).map(JobPostingMapper::toDomain);
    }
}
