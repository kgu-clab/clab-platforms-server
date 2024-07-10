package page.clab.api.domain.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.domain.jobPosting.application.port.in.UpdateJobPostingUseCase;
import page.clab.api.domain.jobPosting.application.port.out.RegisterJobPostingPort;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;

@Service
@RequiredArgsConstructor
public class JobPostingUpdateService implements UpdateJobPostingUseCase {

    private final RetrieveJobPostingPort retrieveJobPostingPort;
    private final RegisterJobPostingPort registerJobPostingPort;

    @Transactional
    @Override
    public Long updateJobPosting(Long jobPostingId, JobPostingUpdateRequestDto requestDto) {
        JobPosting jobPosting = retrieveJobPostingPort.findByIdOrThrow(jobPostingId);
        jobPosting.update(requestDto);
        return registerJobPostingPort.save(jobPosting).getId();
    }
}
