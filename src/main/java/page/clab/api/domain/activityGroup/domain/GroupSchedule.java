package page.clab.api.domain.activityGroup.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
