package page.clab.api.domain.workExperience.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkExperienceUpdateRequestDto {

    @Size(min = 1, message = "{size.workExperience.companyName}")
    @Schema(description = "회사명", example = "네이버 클라우드")
    private String companyName;

    @Size(min = 1, message = "{size.workExperience.position}")
    @Schema(description = "직책", example = "인턴")
    private String position;

    @Schema(description = "시작일", example = "2023-01-01")
    private LocalDate startDate;

    @Schema(description = "종료일", example = "2023-12-31")
    private LocalDate endDate;

}
