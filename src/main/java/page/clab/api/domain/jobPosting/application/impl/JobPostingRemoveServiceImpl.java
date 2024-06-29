package page.clab.api.domain.jobPosting.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.JobPostingRemoveService;
import page.clab.api.domain.jobPosting.dao.JobPostingRepository;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class JobPostingRemoveServiceImpl implements JobPostingRemoveService {

    private final JobPostingRepository jobPostingRepository;

    @Transactional
    @Override
    public Long remove(Long jobPostingId) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        jobPosting.delete();
        return jobPostingRepository.save(jobPosting).getId();
    }

    private JobPosting getJobPostingByIdOrThrow(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채용 공고입니다."));
    }
}