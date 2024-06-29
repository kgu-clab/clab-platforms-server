package page.clab.api.domain.jobPosting.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.jobPosting.domain.JobPosting;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class JobPostingResponseDto {

    private Long id;
    
    private String title;

    private String recruitmentPeriod;

    private String jobPostingUrl;

    private LocalDateTime createdAt;
    
    public static JobPostingResponseDto toDto(JobPosting jobPosting) {
        return JobPostingResponseDto.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle())
                .recruitmentPeriod(jobPosting.getRecruitmentPeriod())
                .jobPostingUrl(jobPosting.getJobPostingUrl())
                .createdAt(jobPosting.getCreatedAt())
                .build();
    }

    public static List<JobPostingResponseDto> toDto(List<JobPosting> jobPostingList) {
        return jobPostingList.stream()
                .map(JobPostingResponseDto::toDto)
                .toList();
    }
}
