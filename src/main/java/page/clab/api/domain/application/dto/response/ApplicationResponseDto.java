package page.clab.api.domain.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    private LocalDateTime updateTime;

    private LocalDateTime createdAt;

    public static ApplicationResponseDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationResponseDto.class);
    }

}
