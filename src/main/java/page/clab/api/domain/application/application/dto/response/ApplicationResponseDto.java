package page.clab.api.domain.application.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponseDto {

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
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public static ApplicationResponseDto toDto(Application application) {
        return ApplicationResponseDto.builder()
                .studentId(application.getStudentId())
                .recruitmentId(application.getRecruitmentId())
                .name(application.getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .department(application.getDepartment())
                .grade(application.getGrade())
                .birth(application.getBirth())
                .address(application.getAddress())
                .interests(application.getInterests())
                .otherActivities(application.getOtherActivities())
                .githubUrl(application.getGithubUrl())
                .applicationType(application.getApplicationType())
                .isPass(application.getIsPass())
                .updatedAt(application.getUpdatedAt())
                .createdAt(application.getCreatedAt())
                .build();
    }
}
