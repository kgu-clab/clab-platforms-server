package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPostingJpaEntity, Long>, JobPostingRepositoryCustom, QuerydslPredicateExecutor<JobPostingJpaEntity> {

    Optional<JobPostingJpaEntity> findByJobPostingUrl(String jobPostingUrl);

    @Query(value = "SELECT j.* FROM job_posting j WHERE j.is_deleted = true", nativeQuery = true)
    Page<JobPostingJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
