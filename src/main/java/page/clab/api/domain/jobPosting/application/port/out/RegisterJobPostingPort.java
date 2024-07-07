package page.clab.api.domain.jobPosting.application.port.out;

import page.clab.api.domain.jobPosting.domain.JobPosting;

public interface RegisterJobPostingPort {
    JobPosting save(JobPosting jobPosting);
}
