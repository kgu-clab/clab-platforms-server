package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberJpaEntity toJpaEntity(Member member);

    Member toDomainEntity(MemberJpaEntity jpaEntity);
}
