package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.etc.ScheduleType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleRequestDto {

    @NotNull
    private ScheduleType scheduleType;

    @NotNull
    private String title;

    @NotNull
    private String detail;

    @NotNull
    @Schema(example = "2023-11-28")
    private LocalDateTime startDate;

    @NotNull
    @Schema(example = "2023-12-28")
    private LocalDateTime endDate;

    private Long activityGroupId;

}
