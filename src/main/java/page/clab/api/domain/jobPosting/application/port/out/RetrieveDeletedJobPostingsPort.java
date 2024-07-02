package page.clab.api.domain.jobPosting.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.jobPosting.domain.JobPosting;

public interface RetrieveDeletedJobPostingsPort {
    Page<JobPosting> findAllByIsDeletedTrue(Pageable pageable);
}