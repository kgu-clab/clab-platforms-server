package page.clab.api.domain.activity.activitygroup.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceResponseDto {

    private Long activityGroupId;
    private String memberId;
    private LocalDateTime attendanceDateTime;
}
