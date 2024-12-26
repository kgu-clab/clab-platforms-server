package page.clab.api.domain.community.hashtag.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    HashtagJpaEntity toEntity(Hashtag hashTag);

    Hashtag toDomain(HashtagJpaEntity entity);
}
