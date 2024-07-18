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
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ActivityGroup activityGroup;

    @Column(nullable = false)
    private LocalDate activityDate;

    public static Attendance create(String memberId, ActivityGroup activityGroup, LocalDate activityDate) {
        return Attendance.builder()
                .memberId(memberId)
                .activityGroup(activityGroup)
                .activityDate(activityDate)
                .build();
    }
}
