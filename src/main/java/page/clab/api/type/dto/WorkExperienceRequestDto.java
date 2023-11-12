package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "회사명", example = "네이버 클라우드", required = true)
    private String companyName;

    @NotNull(message = "{notNull.workExperience.position}")
    @Size(min = 1, message = "{size.workExperience.position}")
    @Schema(description = "직책", example = "인턴", required = true)
    private String position;

    @NotNull(message = "{notNull.workExperience.startDate}")
    @Schema(description = "시작일", example = "2023-01-01", required = true)
    private LocalDate startDate;

    @NotNull(message = "{notNull.workExperience.endDate}")
    @Schema(description = "종료일", example = "2023-12-31", required = true)
    private LocalDate endDate;

}
