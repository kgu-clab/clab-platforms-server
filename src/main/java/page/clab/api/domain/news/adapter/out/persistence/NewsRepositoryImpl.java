package page.clab.api.domain.news.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NewsJpaEntity> findByConditions(String title, String category, Pageable pageable) {
        QNewsJpaEntity news = QNewsJpaEntity.newsJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isEmpty()) builder.and(news.title.containsIgnoreCase(title));
        if (category != null && !category.isEmpty()) builder.and(news.category.eq(category));

        List<NewsJpaEntity> newsList = queryFactory.selectFrom(news)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, news))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(news)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(newsList, pageable, count);
    }
}
