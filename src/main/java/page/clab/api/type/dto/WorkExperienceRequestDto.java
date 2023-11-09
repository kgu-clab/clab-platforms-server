package page.clab.api.type.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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

    @NotNull
    private String companyName;

    @NotNull
    private String position;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
