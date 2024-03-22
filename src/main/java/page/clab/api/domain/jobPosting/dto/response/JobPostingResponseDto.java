package page.clab.api.domain.jobPosting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.jobPosting.domain.JobPosting;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingResponseDto {

    private Long id;
    
    private String title;

    private String recruitmentPeriod;

    private String jobPostingUrl;
    
    public static JobPostingResponseDto toDto(JobPosting jobPosting) {
        return JobPostingResponseDto.builder()
                .id(jobPosting.getId())
                .title(jobPosting.getTitle())
                .recruitmentPeriod(jobPosting.getRecruitmentPeriod())
                .jobPostingUrl(jobPosting.getJobPostingUrl())
                .build();
    }

    public static List<JobPostingResponseDto> toDto(List<JobPosting> jobPostingList) {
        return jobPostingList.stream()
                .map(JobPostingResponseDto::toDto)
                .toList();
    }

}
