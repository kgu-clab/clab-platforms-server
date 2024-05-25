package page.clab.api.domain.blog.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.domain.QBlog;
import page.clab.api.domain.member.domain.QMember;

import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Blog> findByConditions(String title, String memberName, Pageable pageable) {
        QBlog qBlog = QBlog.blog;
        QMember qMember = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isBlank()) builder.and(qBlog.title.containsIgnoreCase(title));
        if (memberName != null && !memberName.isBlank()) builder.and(qMember.name.eq(memberName));

        List<Blog> blogs = queryFactory.selectFrom(qBlog)
                .leftJoin(qBlog.member, qMember)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, "blog"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(qBlog)
                .leftJoin(qBlog.member, qMember)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(blogs, pageable, count);
    }

}
