package page.clab.api.domain.members.donation.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.donation.domain.Donation;

@Mapper(componentModel = "spring")
public interface DonationMapper {

    DonationJpaEntity toJpaEntity(Donation domain);

    Donation toDomain(DonationJpaEntity entity);
}
