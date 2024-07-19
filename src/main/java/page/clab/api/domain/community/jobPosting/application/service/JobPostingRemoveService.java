package page.clab.api.domain.community.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.jobPosting.application.port.in.RemoveJobPostingUseCase;
import page.clab.api.domain.community.jobPosting.application.port.out.RegisterJobPostingPort;
import page.clab.api.domain.community.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;

@Service
@RequiredArgsConstructor
public class JobPostingRemoveService implements RemoveJobPostingUseCase {

    private final RetrieveJobPostingPort retrieveJobPostingPort;
    private final RegisterJobPostingPort registerJobPostingPort;

    @Transactional
    @Override
    public Long removeJobPosting(Long jobPostingId) {
        JobPosting jobPosting = retrieveJobPostingPort.findByIdOrThrow(jobPostingId);
        jobPosting.delete();
        return registerJobPostingPort.save(jobPosting).getId();
    }
}
