package page.clab.api.domain.jobPosting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.RemoveJobPostingUseCase;
import page.clab.api.domain.jobPosting.application.port.out.LoadJobPostingPort;
import page.clab.api.domain.jobPosting.application.port.out.RegisterJobPostingPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;

@Service
@RequiredArgsConstructor
public class JobPostingRemoveService implements RemoveJobPostingUseCase {

    private final LoadJobPostingPort loadJobPostingPort;
    private final RegisterJobPostingPort registerJobPostingPort;

    @Transactional
    @Override
    public Long remove(Long jobPostingId) {
        JobPosting jobPosting = loadJobPostingPort.findByIdOrThrow(jobPostingId);
        jobPosting.delete();
        return registerJobPostingPort.save(jobPosting).getId();
    }
}
