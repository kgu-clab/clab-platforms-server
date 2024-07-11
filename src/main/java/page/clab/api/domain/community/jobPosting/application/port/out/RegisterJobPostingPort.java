package page.clab.api.domain.community.jobPosting.application.port.out;

import page.clab.api.domain.community.jobPosting.domain.JobPosting;

public interface RegisterJobPostingPort {
    JobPosting save(JobPosting jobPosting);
}
