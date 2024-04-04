package page.clab.api.domain.workExperience.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.workExperience.domain.WorkExperience;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class WorkExperienceResponseDto {

    private Long id;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    public static WorkExperienceResponseDto toDto(WorkExperience workExperience) {
        return WorkExperienceResponseDto.builder()
                .id(workExperience.getId())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .startDate(workExperience.getStartDate())
                .endDate(workExperience.getEndDate())
                .build();
    }

}
