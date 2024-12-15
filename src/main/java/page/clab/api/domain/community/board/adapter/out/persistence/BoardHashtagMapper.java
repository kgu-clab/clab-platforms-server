package page.clab.api.domain.community.board.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.board.domain.BoardHashtag;

@Mapper(componentModel = "spring")
public interface BoardHashtagMapper {

    BoardHashtagJpaEntity toEntity(BoardHashtag boardHashTag);

    BoardHashtag toDomain(BoardHashtagJpaEntity entity);
}
