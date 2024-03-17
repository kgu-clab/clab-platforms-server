package page.clab.api.domain.member.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.QMember;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findByConditions(String id, String name, Pageable pageable) {
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (id != null) builder.and(member.id.eq(id));
        if (name != null) builder.and(member.name.eq(name));

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(builder)
                .orderBy(member.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.selectFrom(member)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(members, pageable, totalCount);
    }

    @Override
    public Page<Member> findBirthdaysThisMonth(int month, Pageable pageable) {
        QMember qMember = QMember.member;

        List<Member> members = queryFactory
                .selectFrom(qMember)
                .where(birthdayInMonth(month))
                .orderBy(qMember.birth.dayOfMonth().asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qMember)
                .where(birthdayInMonth(month))
                .fetchCount();

        return new PageImpl<>(members, pageable, total);
    }

    private BooleanExpression birthdayInMonth(int month) {
        return QMember.member.birth.month().eq(month);
    }

}
