package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Application;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequestDto {

    private String studentId;

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

    public static ApplicationRequestDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationRequestDto.class);
    }

}
