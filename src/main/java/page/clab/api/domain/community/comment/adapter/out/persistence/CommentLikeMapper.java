package page.clab.api.domain.community.comment.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.community.comment.domain.CommentLike;

@Mapper(componentModel = "spring")
public interface CommentLikeMapper {

    CommentLikeJpaEntity toJpaEntity(CommentLike commentLike);

    CommentLike toDomain(CommentLikeJpaEntity entity);
}
