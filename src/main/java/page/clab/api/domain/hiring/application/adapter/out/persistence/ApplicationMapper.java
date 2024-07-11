package page.clab.api.domain.hiring.application.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.domain.Application;

@Component
public class ApplicationMapper {

    public ApplicationJpaEntity toJpaEntity(Application application) {
        return ApplicationJpaEntity.builder()
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
                .isDeleted(application.isDeleted())
                .build();
    }

    public Application toDomain(ApplicationJpaEntity entity) {
        return Application.builder()
                .studentId(entity.getStudentId())
                .recruitmentId(entity.getRecruitmentId())
                .name(entity.getName())
                .contact(entity.getContact())
                .email(entity.getEmail())
                .department(entity.getDepartment())
                .grade(entity.getGrade())
                .birth(entity.getBirth())
                .address(entity.getAddress())
                .interests(entity.getInterests())
                .otherActivities(entity.getOtherActivities())
                .githubUrl(entity.getGithubUrl())
                .applicationType(entity.getApplicationType())
                .isPass(entity.getIsPass())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
