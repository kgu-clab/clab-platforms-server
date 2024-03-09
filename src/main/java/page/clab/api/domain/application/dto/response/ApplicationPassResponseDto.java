package page.clab.api.domain.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.global.util.ModelMapperUtil;

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

    public static ApplicationPassResponseDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationPassResponseDto.class);
    }

}
