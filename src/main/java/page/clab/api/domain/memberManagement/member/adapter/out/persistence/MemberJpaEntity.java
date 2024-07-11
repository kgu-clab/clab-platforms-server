package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.domain.memberManagement.member.domain.StudentStatus;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class MemberJpaEntity extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @Size(min = 9, max = 9, message = "{size.member.id}")
    private String id;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.member.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 9, max = 11, message = "{size.member.contact}")
    private String contact;

    @Column(nullable = false)
    @Email(message = "{email.member.email}")
    @Size(min = 1, message = "{size.member.email}")
    private String email;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.member.department}")
    private String department;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.member.grade}")
    @Max(value = 4, message = "{max.member.grade}")
    private Long grade;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.member.address}")
    private String address;

    @Column(nullable = false)
    private String interests;

    @URL(message = "{url.member.githubUrl}")
    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastLoginTime;

    private LocalDateTime loanSuspensionDate;

    private Boolean isOtpEnabled;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
