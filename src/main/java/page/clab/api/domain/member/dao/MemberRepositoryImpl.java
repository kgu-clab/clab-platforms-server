package page.clab.api.domain.member.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.QMember;

import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

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

}
