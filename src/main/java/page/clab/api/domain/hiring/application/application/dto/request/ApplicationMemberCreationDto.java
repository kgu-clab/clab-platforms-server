package page.clab.api.domain.hiring.application.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.Application;

import java.time.LocalDate;

@Getter
@Builder
public class ApplicationMemberCreationDto {

    private String studentId;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;
    private String githubUrl;

    public static ApplicationMemberCreationDto toDto(Application application) {
        return ApplicationMemberCreationDto.builder()
                .studentId(application.getStudentId())
                .name(application.getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .department(application.getDepartment())
                .grade(application.getGrade())
                .birth(application.getBirth())
                .address(application.getAddress())
                .interests(application.getInterests())
                .githubUrl(application.getGithubUrl())
                .build();
    }
}
