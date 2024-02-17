package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGroupReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long turn;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public static ActivityGroupReport of(ActivityGroupReportRequestDto reportRequestDto){
        return ActivityGroupReport.builder()
                .turn(reportRequestDto.getTurn())
                .title(reportRequestDto.getTitle())
                .content(reportRequestDto.getContent())
                .build();
    }

    public void update(ActivityGroupReportUpdateRequestDto reportRequestDto) {
        Optional.ofNullable(reportRequestDto.getTurn()).ifPresent(this::setTurn);
        Optional.ofNullable(reportRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(reportRequestDto.getContent()).ifPresent(this::setContent);
    }

}
