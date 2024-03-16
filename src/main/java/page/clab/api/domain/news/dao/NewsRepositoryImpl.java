package page.clab.api.domain.news.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.domain.QNews;

import java.util.List;

public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NewsRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<News> findByConditions(String title, String category, Pageable pageable) {
        QNews qNews = QNews.news;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isEmpty()) builder.and(qNews.title.containsIgnoreCase(title));
        if (category != null && !category.isEmpty()) builder.and(qNews.category.eq(category));

        List<News> newsList = queryFactory.selectFrom(qNews)
                .where(builder)
                .orderBy(qNews.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(qNews)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(newsList, pageable, count);
    }

}