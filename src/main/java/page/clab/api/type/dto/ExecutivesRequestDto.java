package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.etc.ExecutivesPosition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutivesRequestDto {

    @NotNull(message = "{notNull.executives.memberId}")
    @Schema(description = "학번", example = "201912156", required = true)
    private String memberId;

    @NotNull(message = "{notNull.executives.position}")
    @Schema(description = "직책", example = "OPERATIONS", required = true)
    private ExecutivesPosition position;

    @NotNull(message = "{notNull.executives.year}")
    @Size(min = 4, max = 4, message = "{size.executives.year}")
    @Schema(description = "연도", example = "2023", required = true)
    private String year;

}
