package page.clab.api.domain.jobPosting.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;

public interface RetrieveJobPostingsByConditionsPort {
    Page<JobPosting> findByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable);
}
