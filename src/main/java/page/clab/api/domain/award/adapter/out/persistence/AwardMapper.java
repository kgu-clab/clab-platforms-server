package page.clab.api.domain.award.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.award.domain.Award;

@Component
public class AwardMapper {

    public AwardJpaEntity toJpaEntity(Award award) {
        return AwardJpaEntity.builder()
                .id(award.getId())
                .competitionName(award.getCompetitionName())
                .organizer(award.getOrganizer())
                .awardName(award.getAwardName())
                .awardDate(award.getAwardDate())
                .memberId(award.getMemberId())
                .isDeleted(award.isDeleted())
                .build();
    }

    public Award toDomain(AwardJpaEntity entity) {
        return Award.builder()
                .id(entity.getId())
                .competitionName(entity.getCompetitionName())
                .organizer(entity.getOrganizer())
                .awardName(entity.getAwardName())
                .awardDate(entity.getAwardDate())
                .memberId(entity.getMemberId())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
