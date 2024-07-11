package page.clab.api.domain.activity.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.domain.Absent;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;

@Getter
@Builder
public class AbsentResponseDto {

    private String absenteeId;

    private String absenteeName;

    private Long activityGroupId;

    private String activityGroupName;

    private String reason;

    private LocalDate absentDate;

    public static AbsentResponseDto toDto(Absent absent, Member member) {
        return AbsentResponseDto.builder()
                .absenteeId(member.getId())
                .absenteeName(member.getName())
                .activityGroupId(absent.getActivityGroup().getId())
                .activityGroupName(absent.getActivityGroup().getName())
                .reason(absent.getReason())
                .absentDate(absent.getAbsentDate())
                .build();
    }

}
