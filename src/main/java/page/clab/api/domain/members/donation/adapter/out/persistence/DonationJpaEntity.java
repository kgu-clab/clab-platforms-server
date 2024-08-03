package page.clab.api.domain.members.donation.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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

@Entity
@Table(name = "donation")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE donation SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class DonationJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.donation.amount}")
    private Double amount;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.donation.message}")
    private String message;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
