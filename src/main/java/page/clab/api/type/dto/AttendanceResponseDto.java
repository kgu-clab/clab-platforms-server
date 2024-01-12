package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Attendance;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponseDto {

    private Long activityGroupId;

    private String memberId;

    private LocalDateTime attendanceDateTime;

    public static AttendanceResponseDto of(Attendance attendance){
        AttendanceResponseDto attendanceResponseDto = ModelMapperUtil.getModelMapper().map(attendance, AttendanceResponseDto.class);
        attendanceResponseDto.setActivityGroupId(attendance.getActivityGroup().getId());
        attendanceResponseDto.setMemberId(attendance.getMember().getId());
        attendanceResponseDto.setAttendanceDateTime(attendance.getCreatedAt());

        return attendanceResponseDto;
    }

}
