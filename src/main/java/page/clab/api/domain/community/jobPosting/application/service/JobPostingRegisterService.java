package page.clab.api.domain.community.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.jobPosting.application.dto.request.JobPostingRequestDto;
import page.clab.api.domain.community.jobPosting.application.port.in.RegisterJobPostingUseCase;
import page.clab.api.domain.community.jobPosting.application.port.out.RegisterJobPostingPort;
import page.clab.api.domain.community.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;

@Service
@RequiredArgsConstructor
public class JobPostingRegisterService implements RegisterJobPostingUseCase {

    private final RegisterJobPostingPort registerJobPostingPort;
    private final RetrieveJobPostingPort retrieveJobPostingPort;

    @Transactional
    @Override
    public Long registerJobPosting(JobPostingRequestDto requestDto) {
        JobPosting jobPosting = retrieveJobPostingPort.findByJobPostingUrl(requestDto.getJobPostingUrl())
                .map(existingJobPosting -> existingJobPosting.updateFromRequestDto(requestDto))
                .orElseGet(() -> JobPostingRequestDto.toEntity(requestDto));
        return registerJobPostingPort.save(jobPosting).getId();
    }
}
