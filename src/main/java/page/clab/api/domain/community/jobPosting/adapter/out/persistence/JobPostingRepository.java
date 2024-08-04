package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPostingJpaEntity, Long>, JobPostingRepositoryCustom, QuerydslPredicateExecutor<JobPostingJpaEntity> {
}
