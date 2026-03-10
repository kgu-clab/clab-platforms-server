package page.clab.api.domain.members.support.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.support.domain.SupportAnswer;

@Mapper(componentModel = "spring")
public interface SupportAnswerMapper {

    SupportAnswerJpaEntity toEntity(SupportAnswer supportAnswer);

    SupportAnswer toDomain(SupportAnswerJpaEntity entity);
}
