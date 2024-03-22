package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGroupReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long turn;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public void update(ActivityGroupReportUpdateRequestDto reportRequestDto) {
        Optional.ofNullable(reportRequestDto.getTurn()).ifPresent(this::setTurn);
        Optional.ofNullable(reportRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(reportRequestDto.getContent()).ifPresent(this::setContent);
    }

}
