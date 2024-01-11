package page.clab.api.type.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ActivityGroup activityGroup;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Attendance of(String QRCodeData, Member member){
        AttendanceId attendanceId = AttendanceId.builder()
                .member(member)
                .build();

        return Attendance.builder()
                .attendanceId(attendanceId)
                .build();
    }

}
