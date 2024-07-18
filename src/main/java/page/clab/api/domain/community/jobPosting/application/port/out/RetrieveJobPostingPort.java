package page.clab.api.domain.community.jobPosting.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;

import java.util.Optional;

public interface RetrieveJobPostingPort {

    JobPosting findByIdOrThrow(Long jobPostingId);

    Page<JobPosting> findAllByIsDeletedTrue(Pageable pageable);

    Page<JobPosting> findByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable);

    Optional<JobPosting> findByJobPostingUrl(String jobPostingUrl);
}
