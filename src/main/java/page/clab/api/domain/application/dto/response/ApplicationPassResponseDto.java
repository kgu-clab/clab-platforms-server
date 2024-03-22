package page.clab.api.domain.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationPassResponseDto {

    private Long recruitmentId;

    private String name;

    private ApplicationType applicationType;

    private Boolean isPass;

    public static ApplicationPassResponseDto toDto(Application application) {
        return ApplicationPassResponseDto.builder()
                .recruitmentId(application.getRecruitmentId())
                .name(application.getName())
                .applicationType(application.getApplicationType())
                .isPass(application.getIsPass())
                .build();
    }

}
