package page.clab.api.domain.blog.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.memberManagement.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BlogJpaEntity> findByConditions(String title, String memberName, Pageable pageable) {
        QBlogJpaEntity blog = QBlogJpaEntity.blogJpaEntity;
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isBlank()) builder.and(blog.title.containsIgnoreCase(title));
        if (memberName != null && !memberName.isBlank()) builder.and(member.name.eq(memberName));

        List<BlogJpaEntity> blogs = queryFactory.selectFrom(blog)
                .leftJoin(member).on(blog.memberId.eq(member.id))
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, blog))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(blog)
                .leftJoin(member).on(blog.memberId.eq(member.id))
                .where(builder)
                .fetchCount();

        return new PageImpl<>(blogs, pageable, count);
    }
}
