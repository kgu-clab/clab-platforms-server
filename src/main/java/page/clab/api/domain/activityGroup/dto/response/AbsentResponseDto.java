package page.clab.api.domain.activityGroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.Absent;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AbsentResponseDto {

    private String absenteeId;

    private String absenteeName;

    private Long activityGroupId;

    private String activityGroupName;

    private String reason;

    private LocalDate absentDate;

    public static AbsentResponseDto toDto(Absent absent) {
        return AbsentResponseDto.builder()
                .absenteeId(absent.getAbsentee().getId())
                .absenteeName(absent.getAbsentee().getName())
                .activityGroupId(absent.getActivityGroup().getId())
                .activityGroupName(absent.getActivityGroup().getName())
                .reason(absent.getReason())
                .absentDate(absent.getAbsentDate())
                .build();
    }

}
