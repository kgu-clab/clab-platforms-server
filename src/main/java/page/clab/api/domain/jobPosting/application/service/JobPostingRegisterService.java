package page.clab.api.domain.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.RegisterJobPostingUseCase;
import page.clab.api.domain.jobPosting.application.port.out.RegisterJobPostingPort;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class JobPostingRegisterService implements RegisterJobPostingUseCase {

    private final ValidationService validationService;
    private final RegisterJobPostingPort registerJobPostingPort;
    private final RetrieveJobPostingPort retrieveJobPostingPort;

    @Transactional
    @Override
    public Long register(JobPostingRequestDto requestDto) {
        JobPosting jobPosting = retrieveJobPostingPort.findByJobPostingUrl(requestDto.getJobPostingUrl())
                .map(existingJobPosting -> existingJobPosting.updateFromRequestDto(requestDto))
                .orElseGet(() -> JobPostingRequestDto.toEntity(requestDto));
        validationService.checkValid(jobPosting);
        return registerJobPostingPort.save(jobPosting).getId();
    }
}