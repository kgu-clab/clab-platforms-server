package page.clab.api.domain.activity.dto.param;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.GroupSchedule;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GroupScheduleDto {

    private LocalDateTime schedule;

    private String content;

    public static GroupScheduleDto toDto(GroupSchedule groupSchedule) {
        return GroupScheduleDto.builder()
                .schedule(groupSchedule.getSchedule())
                .content(groupSchedule.getContent())
                .build();
    }

    public static GroupSchedule toEntity(GroupScheduleDto groupScheduleDto, ActivityGroup activityGroup) {
        return GroupSchedule.builder()
                .activityGroup(activityGroup)
                .schedule(groupScheduleDto.getSchedule())
                .content(groupScheduleDto.getContent())
                .build();
    }

}
