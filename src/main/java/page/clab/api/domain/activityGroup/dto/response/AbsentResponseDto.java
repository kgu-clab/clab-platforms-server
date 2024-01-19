package page.clab.api.domain.activityGroup.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.Absent;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsentResponseDto {

    private String absenteeId;

    private String absenteeName;

    private Long activityGroupId;

    private String activityGroupName;

    private String reason;

    private LocalDate absentDate;

    public static AbsentResponseDto of(Absent absent) {
        return ModelMapperUtil.getModelMapper().map(absent, AbsentResponseDto.class);
    }

}
