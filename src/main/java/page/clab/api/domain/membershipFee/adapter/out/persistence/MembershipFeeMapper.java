package page.clab.api.domain.membershipFee.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.membershipFee.domain.MembershipFee;

@Component
public class MembershipFeeMapper {

    public MembershipFeeJpaEntity toJpaEntity(MembershipFee membershipFee) {
        return MembershipFeeJpaEntity.builder()
                .id(membershipFee.getId())
                .memberId(membershipFee.getMemberId())
                .category(membershipFee.getCategory())
                .account(membershipFee.getAccount())
                .amount(membershipFee.getAmount())
                .content(membershipFee.getContent())
                .imageUrl(membershipFee.getImageUrl())
                .status(membershipFee.getStatus())
                .isDeleted(membershipFee.isDeleted())
                .build();
    }

    public MembershipFee toDomainEntity(MembershipFeeJpaEntity jpaEntity) {
        return MembershipFee.builder()
                .id(jpaEntity.getId())
                .memberId(jpaEntity.getMemberId())
                .category(jpaEntity.getCategory())
                .account(jpaEntity.getAccount())
                .amount(jpaEntity.getAmount())
                .content(jpaEntity.getContent())
                .imageUrl(jpaEntity.getImageUrl())
                .status(jpaEntity.getStatus())
                .isDeleted(jpaEntity.isDeleted())
                .build();
    }
}
