package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    @EmbeddedId
    private AttendanceId attendanceId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Attendance of(String QRCodeData, Member member){
        AttendanceId attendanceId = AttendanceId.builder()
                .member(member)
                .QRCodeData(QRCodeData)
                .build();

        return Attendance.builder()
                .attendanceId(attendanceId)
                .build();
    }

}
