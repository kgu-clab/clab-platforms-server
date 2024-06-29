package page.clab.api.domain.jobPosting.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.jobPosting.domain.JobPosting;

import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long>, JobPostingRepositoryCustom, QuerydslPredicateExecutor<JobPosting> {

    Optional<JobPosting> findByJobPostingUrl(String jobPostingUrl);

    Page<JobPosting> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT j.* FROM job_posting j WHERE j.is_deleted = true", nativeQuery = true)
    Page<JobPosting> findAllByIsDeletedTrue(Pageable pageable);
}
