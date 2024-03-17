package page.clab.api.domain.application.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.QApplication;

import java.util.List;

@Repository
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public ApplicationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        QApplication qApplication = QApplication.application;
        BooleanBuilder builder = new BooleanBuilder();

        if (recruitmentId != null) builder.and(qApplication.recruitmentId.eq(recruitmentId));
        if (studentId != null && !studentId.isEmpty()) builder.and(qApplication.studentId.eq(studentId));
        if (isPass != null) builder.and(qApplication.isPass.eq(isPass));

        List<Application> applications = queryFactory.selectFrom(qApplication)
                .where(builder.getValue())
                .orderBy(qApplication.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.from(qApplication)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(applications, pageable, count);
    }

}
