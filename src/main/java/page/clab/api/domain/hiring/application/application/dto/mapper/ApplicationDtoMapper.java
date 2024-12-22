package page.clab.api.domain.hiring.application.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationMemberCreationDto;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.hiring.application.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.hiring.application.application.dto.response.ApplicationResponseDto;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.global.common.domain.Contact;

@Component
public class ApplicationDtoMapper {

    public Application fromDto(ApplicationRequestDto requestDto) {
        return Application.builder()
            .studentId(requestDto.getStudentId())
            .recruitmentId(requestDto.getRecruitmentId())
            .name(requestDto.getName())
            .contact(Contact.of(requestDto.getContact()).getValue())
            .email(requestDto.getEmail())
            .department(requestDto.getDepartment())
            .grade(requestDto.getGrade())
            .birth(requestDto.getBirth())
            .address(requestDto.getAddress())
            .interests(requestDto.getInterests())
            .otherActivities(requestDto.getOtherActivities())
            .githubUrl(requestDto.getGithubUrl())
            .applicationType(requestDto.getApplicationType())
            .isPass(false)
            .isDeleted(false)
            .build();
    }

    public ApplicationResponseDto toDto(Application application) {
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

    public ApplicationMemberCreationDto toCreationDto(Application application) {
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

    public ApplicationPassResponseDto toPassDto(Application application) {
        return ApplicationPassResponseDto.builder()
            .recruitmentId(application.getRecruitmentId())
            .name(application.getName())
            .applicationType(application.getApplicationType())
            .isPass(application.getIsPass())
            .build();
    }
}
