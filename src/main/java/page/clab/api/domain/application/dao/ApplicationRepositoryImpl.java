package page.clab.api.domain.application.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.QApplication;

import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        QApplication qApplication = QApplication.application;
        BooleanBuilder builder = new BooleanBuilder();

        if (recruitmentId != null) builder.and(qApplication.recruitmentId.eq(recruitmentId));
        if (studentId != null && !studentId.isEmpty()) builder.and(qApplication.studentId.eq(studentId));
        if (isPass != null) builder.and(qApplication.isPass.eq(isPass));

        List<Application> applications = queryFactory.selectFrom(qApplication)
                .where(builder.getValue())
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qApplication))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.from(qApplication)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(applications, pageable, count);
    }

}
