package page.clab.api.domain.hiring.application.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.domain.memberManagement.member.domain.StudentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Application {

    private String studentId;
    private Long recruitmentId;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;
    private String otherActivities;
    private String githubUrl;
    private ApplicationType applicationType;
    private Boolean isPass;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Member toMember(Application application) {
        return Member.builder()
                .id(application.getStudentId())
                .name(application.getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .department(application.getDepartment())
                .grade(application.getGrade())
                .birth(application.getBirth())
                .address(application.getAddress())
                .interests(application.getInterests())
                .githubUrl(application.getGithubUrl())
                .studentStatus(StudentStatus.CURRENT)
                .imageUrl("")
                .role(Role.USER)
                .isOtpEnabled(false)
                .isDeleted(false)
                .build();
    }

    public void toggleApprovalStatus() {
        this.isPass = !this.isPass;
    }
}
