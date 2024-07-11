package page.clab.api.domain.community.jobPosting.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "job_posting")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE job_posting SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class JobPostingJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.jobPosting.title}")
    private String title;

    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.jobPosting.companyName}")
    private String companyName;

    private String recruitmentPeriod;

    @Column(nullable = false)
    @Size(max = 1000, message = "{size.jobPosting.jobPostingUrl}")
    @URL(message = "{url.jobPosting.jobPostingUrl}")
    private String jobPostingUrl;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
