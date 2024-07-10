package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.Absent;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;

@Getter
@Setter
public class AbsentRequestDto {

    @NotNull(message = "{notNull.absent.absenteeId}")
    @Schema(description = "불참 학생 학번", example = "202311220", required = true)
    private String absenteeId;

    @NotNull(message = "{notNull.absent.activityGroupId}")
    @Schema(description = "불참 그룹 아이디", example = "1", required = true)
    private Long activityGroupId;

    @NotNull(message = "{notNull.absent.reason}")
    @Schema(description = "불참 사유", example = "독감", required = true)
    private String reason;

    @NotNull(message = "{notNull.absent.absentDate}")
    @Schema(description = "불참 날짜", example = "2023-11-12", required = true)
    private LocalDate absentDate;

    public static Absent toEntity(AbsentRequestDto requestDto, Member absentee, ActivityGroup activityGroup) {
        return Absent.builder()
                .memberId(absentee.getId())
                .activityGroup(activityGroup)
                .absentDate(requestDto.getAbsentDate())
                .reason(requestDto.getReason())
                .build();

    }

}
