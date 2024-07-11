package page.clab.api.domain.memberManagement.award.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "award")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE award SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class AwardJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "{size.award.competitionName}")
    private String competitionName;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "{size.award.organizer}")
    private String organizer;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "{size.award.awardName}")
    private String awardName;

    @Column(nullable = false)
    private LocalDate awardDate;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
