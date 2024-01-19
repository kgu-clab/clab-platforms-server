package page.clab.api.domain.executive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.executive.domain.ExecutivePosition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutivesRequestDto {

    @NotNull(message = "{notNull.executive.memberId}")
    @Schema(description = "학번", example = "201912156", required = true)
    private String memberId;

    @NotNull(message = "{notNull.executive.position}")
    @Schema(description = "직책", example = "OPERATIONS", required = true)
    private ExecutivePosition position;

    @NotNull(message = "{notNull.executive.year}")
    @Size(min = 4, max = 4, message = "{size.executive.year}")
    @Schema(description = "연도", example = "2023", required = true)
    private String year;

}
