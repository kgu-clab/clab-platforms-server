package page.clab.api.domain.community.news.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.news.domain.News;

@Component
public class NewsMapper {

    public NewsJpaEntity toJpaEntity(News news) {
        return NewsJpaEntity.builder()
                .id(news.getId())
                .title(news.getTitle())
                .category(news.getCategory())
                .content(news.getContent())
                .articleUrl(news.getArticleUrl())
                .source(news.getSource())
                .date(news.getDate())
                .isDeleted(news.isDeleted())
                .build();
    }

    public News toDomainEntity(NewsJpaEntity jpaEntity) {
        return News.builder()
                .id(jpaEntity.getId())
                .title(jpaEntity.getTitle())
                .category(jpaEntity.getCategory())
                .content(jpaEntity.getContent())
                .articleUrl(jpaEntity.getArticleUrl())
                .source(jpaEntity.getSource())
                .date(jpaEntity.getDate())
                .isDeleted(jpaEntity.isDeleted())
                .build();
    }
}
