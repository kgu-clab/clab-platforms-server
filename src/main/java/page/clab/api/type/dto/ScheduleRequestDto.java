package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleRequestDto {
    @NotNull
    private String title;

    @NotNull
    private String detail;


    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;


    private Long activityGroupId;


}
