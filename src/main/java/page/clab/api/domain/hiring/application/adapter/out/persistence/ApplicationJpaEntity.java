package page.clab.api.domain.hiring.application.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
import page.clab.api.domain.hiring.application.domain.ApplicationType;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "application")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(ApplicationId.class)
@SQLDelete(sql = "UPDATE application SET is_deleted = true WHERE recruitment_id = ? AND student_id = ?")
@SQLRestriction("is_deleted = false")
public class ApplicationJpaEntity extends BaseEntity {

    @Id
    @Size(min = 9, max = 9, message = "{size.application.studentId}")
    @Pattern(regexp = "^[0-9]+$", message = "{pattern.application.studentId}")
    private String studentId;

    @Id
    private Long recruitmentId;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.application.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 9, max = 11, message = "{size.application.contact}")
    private String contact;

    @Column(nullable = false)
    @Email(message = "{email.application.email}")
    @Size(min = 1, message = "{size.application.email}")
    private String email;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.application.department}")
    private String department;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.application.grade}")
    @Max(value = 4, message = "{max.application.grade}")
    private Long grade;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.application.address}")
    private String address;

    @Column(nullable = false)
    private String interests;

    @Column(nullable = false, length = 1000)
    @Size(max = 1000, message = "{size.application.otherActivities}")
    private String otherActivities;

    @URL(message = "{url.application.githubUrl}")
    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(nullable = false)
    private Boolean isPass;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
