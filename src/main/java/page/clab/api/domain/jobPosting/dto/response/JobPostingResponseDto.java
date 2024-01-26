package page.clab.api.domain.jobPosting.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.global.util.ModelMapperUtil;

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
    
    public static JobPostingResponseDto of(JobPosting jobPosting) {
        return ModelMapperUtil.getModelMapper().map(jobPosting, JobPostingResponseDto.class);
    }

    public static List<JobPostingResponseDto> of(List<JobPosting> jobPostingList) {
        return jobPostingList.stream()
                .map(JobPostingResponseDto::of)
                .toList();
    }

}
