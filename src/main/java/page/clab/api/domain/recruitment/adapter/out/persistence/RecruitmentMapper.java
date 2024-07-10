package page.clab.api.domain.recruitment.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Component
public class RecruitmentMapper {

    public RecruitmentJpaEntity toJpaEntity(Recruitment recruitment) {
        return RecruitmentJpaEntity.builder()
                .id(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .applicationType(recruitment.getApplicationType())
                .target(recruitment.getTarget())
                .status(recruitment.getStatus())
                .isDeleted(recruitment.isDeleted())
                .build();
    }

    public Recruitment toDomainEntity(RecruitmentJpaEntity entity) {
        return Recruitment.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .applicationType(entity.getApplicationType())
                .target(entity.getTarget())
                .status(entity.getStatus())
                .isDeleted(entity.isDeleted())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
