package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Application;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationPassResponseDto {

    private String name;

    private Boolean isPass;

    public static ApplicationPassResponseDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationPassResponseDto.class);
    }

}
