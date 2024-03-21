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
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ActivityGroup activityGroup;

    @Column(nullable = false)
    private LocalDate activityDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Attendance create(Member member, ActivityGroup activityGroup, LocalDate activityDate) {
        return Attendance.builder()
                .member(member)
                .activityGroup(activityGroup)
                .activityDate(activityDate)
                .build();
    }

}
