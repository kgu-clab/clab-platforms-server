package page.clab.api.domain.donation.adapter.out.persistence;

import page.clab.api.domain.donation.domain.Donation;

public class DonationMapper {

    public static DonationJpaEntity toJpaEntity(Donation domain) {
        return DonationJpaEntity.builder()
                .id(domain.getId())
                .memberId(domain.getMemberId())
                .amount(domain.getAmount())
                .message(domain.getMessage())
                .isDeleted(domain.isDeleted())
                .build();
    }

    public static Donation toDomain(DonationJpaEntity entity) {
        return Donation.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .amount(entity.getAmount())
                .message(entity.getMessage())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
