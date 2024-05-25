package page.clab.api.domain.jobPosting.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.domain.QJobPosting;

import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class JobPostingRepositoryImpl implements JobPostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<JobPosting> findByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable) {
        QJobPosting qJobPosting = QJobPosting.jobPosting;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isEmpty()) builder.and(qJobPosting.title.containsIgnoreCase(title));
        if (companyName != null && !companyName.isEmpty()) builder.and(qJobPosting.companyName.containsIgnoreCase(companyName));
        if (careerLevel != null) builder.and(qJobPosting.careerLevel.eq(careerLevel));
        if (employmentType != null) builder.and(qJobPosting.employmentType.eq(employmentType));

        List<JobPosting> jobPostings = queryFactory.selectFrom(qJobPosting)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qJobPosting))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.query().from(qJobPosting)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(jobPostings, pageable, total);
    }

}