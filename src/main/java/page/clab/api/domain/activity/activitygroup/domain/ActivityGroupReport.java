package page.clab.api.domain.activity.activitygroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE activity_group_report SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
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

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public void update(ActivityGroupReportUpdateRequestDto reportRequestDto) {
        Optional.ofNullable(reportRequestDto.getTurn()).ifPresent(this::setTurn);
        Optional.ofNullable(reportRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(reportRequestDto.getContent()).ifPresent(this::setContent);
    }
}
