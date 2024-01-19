package page.clab.api.domain.activityGroup.dto.param;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupScheduleDto {

    private LocalDateTime schedule;

    private String content;

    public static GroupScheduleDto of(GroupSchedule groupSchedule) {
        return ModelMapperUtil.getModelMapper().map(groupSchedule, GroupScheduleDto.class);
    }
}
