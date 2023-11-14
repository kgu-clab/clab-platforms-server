package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.util.ModelMapperUtil;

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
