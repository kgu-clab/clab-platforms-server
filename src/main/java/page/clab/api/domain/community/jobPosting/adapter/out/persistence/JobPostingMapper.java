package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import page.clab.api.domain.community.jobPosting.domain.JobPosting;

public class JobPostingMapper {

    public static JobPostingJpaEntity toJpaEntity(JobPosting domain) {
        return JobPostingJpaEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .careerLevel(domain.getCareerLevel())
                .employmentType(domain.getEmploymentType())
                .companyName(domain.getCompanyName())
                .recruitmentPeriod(domain.getRecruitmentPeriod())
                .jobPostingUrl(domain.getJobPostingUrl())
                .isDeleted(domain.isDeleted())
                .build();
    }

    public static JobPosting toDomain(JobPostingJpaEntity entity) {
        return JobPosting.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .careerLevel(entity.getCareerLevel())
                .employmentType(entity.getEmploymentType())
                .companyName(entity.getCompanyName())
                .recruitmentPeriod(entity.getRecruitmentPeriod())
                .jobPostingUrl(entity.getJobPostingUrl())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
