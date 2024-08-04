package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;

public interface JobPostingRepositoryCustom {
    Page<JobPostingJpaEntity> findByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable);
}
