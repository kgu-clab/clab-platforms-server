package page.clab.api.domain.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.dto.request.ScheduleRequestDto;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @NotNull
    private String title;

    @NotNull
    private String detail;

    @NotNull
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @NotNull
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member scheduleWriter;

    @ManyToOne
    @JoinColumn(name = "activityGroup")
    private ActivityGroup activityGroup;

    public static Schedule of(ScheduleRequestDto scheduleRequestDto) {
        return Schedule.builder()
                .scheduleType(scheduleRequestDto.getScheduleType())
                .title(scheduleRequestDto.getTitle())
                .detail(scheduleRequestDto.getDetail())
                .startDateTime(scheduleRequestDto.getStartDateTime())
                .endDateTime(scheduleRequestDto.getEndDateTime())
                .build();
    }

}
