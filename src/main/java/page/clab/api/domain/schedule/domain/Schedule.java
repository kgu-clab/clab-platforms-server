package page.clab.api.domain.schedule.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
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

    private Long activityGroupId;

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
