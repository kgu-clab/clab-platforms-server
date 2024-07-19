package page.clab.api.domain.members.membershipFee.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "membership_fee")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE membership_fee SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class MembershipFeeJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.membershipFee.category}")
    private String category;

    private String account;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.membershipFee.content}")
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipFeeStatus status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
