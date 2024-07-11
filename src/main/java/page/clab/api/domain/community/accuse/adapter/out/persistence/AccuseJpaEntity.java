package page.clab.api.domain.community.accuse.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "accuse")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE accuse SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class AccuseJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "target_type", nullable = false),
            @JoinColumn(name = "target_reference_id", nullable = false)
    })
    private AccuseTargetJpaEntity target;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.accuse.reason}")
    private String reason;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
