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
import lombok.ToString;
import page.clab.api.domain.activityGroup.dto.request.AbsentRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Absent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member absentee;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @Column(nullable = false)
    private LocalDate absentDate;

    @Column(nullable = false)
    private String reason;

    public static Absent create(Member absentee, ActivityGroup activityGroup, AbsentRequestDto absentRequestDto) {
        return Absent.builder()
                .id(null)
                .absentee(absentee)
                .activityGroup(activityGroup)
                .absentDate(absentRequestDto.getAbsentDate())
                .reason(absentRequestDto.getReason())
                .build();
    }

}
