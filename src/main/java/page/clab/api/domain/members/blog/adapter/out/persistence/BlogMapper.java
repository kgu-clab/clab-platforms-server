package page.clab.api.domain.members.blog.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.blog.domain.Blog;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    BlogJpaEntity toJpaEntity(Blog blog);

    Blog toDomain(BlogJpaEntity entity);
}
