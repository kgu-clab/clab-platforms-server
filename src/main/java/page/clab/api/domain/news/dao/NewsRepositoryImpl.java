package page.clab.api.domain.news.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.domain.QNews;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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