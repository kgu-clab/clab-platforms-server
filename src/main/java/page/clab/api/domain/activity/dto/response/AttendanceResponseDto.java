package page.clab.api.domain.activity.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.domain.Attendance;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResponseDto {

    private Long activityGroupId;

    private String memberId;

    private LocalDateTime attendanceDateTime;

    public static AttendanceResponseDto toDto(Attendance attendance) {
        return AttendanceResponseDto.builder()
                .activityGroupId(attendance.getActivityGroup().getId())
                .memberId(attendance.getMemberId())
                .attendanceDateTime(attendance.getCreatedAt())
                .build();
    }

}
