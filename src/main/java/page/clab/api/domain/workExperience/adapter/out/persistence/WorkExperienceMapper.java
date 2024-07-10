package page.clab.api.domain.workExperience.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.workExperience.domain.WorkExperience;

@Component
public class WorkExperienceMapper {

    public WorkExperienceJpaEntity toJpaEntity(WorkExperience workExperience) {
        return WorkExperienceJpaEntity.builder()
                .id(workExperience.getId())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .startDate(workExperience.getStartDate())
                .endDate(workExperience.getEndDate())
                .memberId(workExperience.getMemberId())
                .isDeleted(workExperience.isDeleted())
                .build();
    }

    public WorkExperience toDomainEntity(WorkExperienceJpaEntity jpaEntity) {
        return WorkExperience.builder()
                .id(jpaEntity.getId())
                .companyName(jpaEntity.getCompanyName())
                .position(jpaEntity.getPosition())
                .startDate(jpaEntity.getStartDate())
                .endDate(jpaEntity.getEndDate())
                .memberId(jpaEntity.getMemberId())
                .isDeleted(jpaEntity.isDeleted())
                .build();
    }
}
