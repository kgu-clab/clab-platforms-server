package page.clab.api.domain.jobPosting.application.port.out;

import page.clab.api.domain.jobPosting.domain.JobPosting;

import java.util.Optional;

public interface RetrieveJobPostingByUrlPort {
    Optional<JobPosting> findByJobPostingUrl(String jobPostingUrl);
}
