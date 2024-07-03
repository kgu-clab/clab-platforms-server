package page.clab.api.domain.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.UpdateJobPostingUseCase;
import page.clab.api.domain.jobPosting.dao.JobPostingRepository;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class JobPostingUpdateService implements UpdateJobPostingUseCase {

    private final ValidationService validationService;
    private final JobPostingRepository jobPostingRepository;

    @Transactional
    @Override
    public Long update(Long jobPostingId, JobPostingUpdateRequestDto requestDto) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        jobPosting.update(requestDto);
        validationService.checkValid(jobPosting);
        return jobPostingRepository.save(jobPosting).getId();
    }

    private JobPosting getJobPostingByIdOrThrow(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채용 공고입니다."));
    }
}
