package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_group_id")
    private ActivityGroup activityGroup;

    @Column(nullable = false)
    private LocalDateTime schedule;

    @Column(nullable = false)
    private String content;

    public static GroupSchedule of(ActivityGroup activityGroup, GroupScheduleDto groupScheduleDto) {
        return GroupSchedule.builder()
                .activityGroup(activityGroup)
                .schedule(groupScheduleDto.getSchedule())
                .content(groupScheduleDto.getContent())
                .build();
    }

}
