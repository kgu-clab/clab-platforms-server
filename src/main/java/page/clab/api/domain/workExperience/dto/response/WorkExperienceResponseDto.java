package page.clab.api.domain.workExperience.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkExperienceResponseDto {

    private Long id;

    private String companyName;

    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    public static WorkExperienceResponseDto of(WorkExperience workExperience) {
        return ModelMapperUtil.getModelMapper().map(workExperience, WorkExperienceResponseDto.class);
    }

}
