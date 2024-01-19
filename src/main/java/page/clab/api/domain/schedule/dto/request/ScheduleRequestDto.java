package page.clab.api.domain.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.schedule.domain.ScheduleType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleRequestDto {

    @NotNull(message = "{notNull.schedule.scheduleType}")
    @Schema(description = "일정 타입", example = "ALL")
    private ScheduleType scheduleType;

    @NotNull(message = "{notNull.schedule.title}")
    @Schema(description = "일정 제목", example = "동아리 개강총회")
    private String title;

    @NotNull(message = "{notNull.schedule.detail}")
    @Schema(description = "일정 세부사항", example = "학기를 C-Lab과 함께 시작할 수 있는 자리!")
    private String detail;

    @NotNull(message = "{notNull.schedule.startDate}")
    @Schema(description = "일정 시작날짜와 시간", example = "2023-11-28 18:00:00.000")
    private LocalDateTime startDateTime;

    @NotNull(message = "{notNull.schedule.endDate}")
    @Schema(description = "일정 종료날짜와 시간", example = "2023-11-28 22:00:00.000")
    private LocalDateTime endDateTime;

    private Long activityGroupId;

}
