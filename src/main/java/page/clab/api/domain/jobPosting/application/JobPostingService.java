package page.clab.api.domain.jobPosting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.dao.JobPostingRepository;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final ValidationService validationService;

    private final JobPostingRepository jobPostingRepository;

    @Transactional
    public Long createJobPosting(JobPostingRequestDto requestDto) {
        JobPosting jobPosting = jobPostingRepository.findByJobPostingUrl(requestDto.getJobPostingUrl())
                .map(existingJobPosting -> existingJobPosting.updateFromRequestDto(requestDto))
                .orElseGet(() -> JobPostingRequestDto.toEntity(requestDto));
        validationService.checkValid(jobPosting);
        return jobPostingRepository.save(jobPosting).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<JobPostingResponseDto> getJobPostingsByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable) {
        Page<JobPosting> jobPostings = jobPostingRepository.findByConditions(title, companyName, careerLevel, employmentType, pageable);
        return new PagedResponseDto<>(jobPostings.map(JobPostingResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public JobPostingDetailsResponseDto getJobPosting(Long jobPostingId) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        return JobPostingDetailsResponseDto.toDto(jobPosting);
    }

    @Transactional
    public Long updateJobPosting(Long jobPostingId, JobPostingUpdateRequestDto requestDto) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        jobPosting.update(requestDto);
        validationService.checkValid(jobPosting);
        return jobPostingRepository.save(jobPosting).getId();
    }

    public Long deleteJobPosting(Long jobPostingId) {
        JobPosting jobPosting = getJobPostingByIdOrThrow(jobPostingId);
        jobPostingRepository.delete(jobPosting);
        return jobPostingId;
    }

    public JobPosting getJobPostingByIdOrThrow(Long jobPostingId) {
        return jobPostingRepository.findById(jobPostingId)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 채용 공고입니다."));
    }

}
