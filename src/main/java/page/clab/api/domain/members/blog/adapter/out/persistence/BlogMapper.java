package page.clab.api.domain.members.blog.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.members.blog.domain.Blog;

@Component
public class BlogMapper {

    public BlogJpaEntity toJpaEntity(Blog blog) {
        return BlogJpaEntity.builder()
                .id(blog.getId())
                .memberId(blog.getMemberId())
                .title(blog.getTitle())
                .subTitle(blog.getSubTitle())
                .content(blog.getContent())
                .imageUrl(blog.getImageUrl())
                .hyperlink(blog.getHyperlink())
                .isDeleted(blog.isDeleted())
                .build();
    }

    public Blog toDomain(BlogJpaEntity entity) {
        return Blog.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .title(entity.getTitle())
                .subTitle(entity.getSubTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .hyperlink(entity.getHyperlink())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
