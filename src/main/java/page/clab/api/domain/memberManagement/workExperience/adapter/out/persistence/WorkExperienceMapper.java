package page.clab.api.domain.memberManagement.workExperience.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {

    WorkExperienceJpaEntity toJpaEntity(WorkExperience workExperience);

    WorkExperience toDomainEntity(WorkExperienceJpaEntity jpaEntity);
}
