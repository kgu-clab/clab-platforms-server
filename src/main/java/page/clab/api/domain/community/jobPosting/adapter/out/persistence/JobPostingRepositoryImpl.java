package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class JobPostingRepositoryImpl implements JobPostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<JobPostingJpaEntity> findByConditions(String title, String companyName, CareerLevel careerLevel,
        EmploymentType employmentType, Pageable pageable) {
        QJobPostingJpaEntity jobPosting = QJobPostingJpaEntity.jobPostingJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isEmpty()) {
            builder.and(jobPosting.title.containsIgnoreCase(title));
        }
        if (companyName != null && !companyName.isEmpty()) {
            builder.and(jobPosting.companyName.containsIgnoreCase(companyName));
        }
        if (careerLevel != null) {
            builder.and(jobPosting.careerLevel.eq(careerLevel));
        }
        if (employmentType != null) {
            builder.and(jobPosting.employmentType.eq(employmentType));
        }

        List<JobPostingJpaEntity> jobPostings = queryFactory.selectFrom(jobPosting)
            .where(builder)
            .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, jobPosting))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory.query().from(jobPosting)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(jobPostings, pageable, total);
    }
}
