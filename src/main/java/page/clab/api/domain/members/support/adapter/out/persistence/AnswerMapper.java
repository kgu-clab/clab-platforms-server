package page.clab.api.domain.members.support.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.support.domain.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    AnswerJpaEntity toEntity(Answer support);
    Answer toDomain(AnswerJpaEntity entity);
}
