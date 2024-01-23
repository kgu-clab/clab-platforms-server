package page.clab.api.domain.jobPosting.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.jobPosting.domain.JobPosting;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Optional<JobPosting> findByJobPostingUrl(String jobPostingUrl);

    Page<JobPosting> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
}
