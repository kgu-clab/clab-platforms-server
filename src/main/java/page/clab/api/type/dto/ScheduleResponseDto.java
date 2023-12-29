package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Schedule;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponseDto {

    private Long id;

    private String title;

    private String detail;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    public static ScheduleResponseDto of(Schedule schedule){
        ScheduleResponseDto scheduleResponseDto = ModelMapperUtil.getModelMapper()
                .map(schedule, ScheduleResponseDto.class);

        return scheduleResponseDto;
    }

}
