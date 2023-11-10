package page.clab.api.type.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkExperienceRequestDto {

    @NotNull(message = "{notNull.workExperience.companyName}")
    @Size(min = 1, message = "{size.workExperience.companyName}")
    private String companyName;

    @NotNull(message = "{notNull.workExperience.position}")
    @Size(min = 1, message = "{size.workExperience.position}")
    private String position;

    @NotNull(message = "{notNull.workExperience.startDate}")
    private LocalDate startDate;

    @NotNull(message = "{notNull.workExperience.endDate}")
    private LocalDate endDate;

}
