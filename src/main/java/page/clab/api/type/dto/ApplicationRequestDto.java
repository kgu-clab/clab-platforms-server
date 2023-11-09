package page.clab.api.type.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.entity.Application;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequestDto {

    @Size(min = 9, max = 9, message = "{size.application.studentId}")
    @Pattern(regexp = "^[0-9]+$", message = "{pattern.application.studentId}")
    private String studentId;

    @NotNull(message = "{notNull.application.name}")
    @Size(min = 1, max = 10, message = "{size.application.name}")
    private String name;

    @NotNull(message = "{notNull.application.contact}")
    @Size(min = 11, max = 11, message = "{size.application.contact}")
    private String contact;

    @NotNull(message = "{notNull.application.email}")
    @Email(message = "{email.application.email}")
    private String email;

    @NotNull(message = "{notNull.application.department}")
    private String department;

    @NotNull(message = "{notNull.application.grade}")
    @Min(value = 1, message = "{min.application.grade}")
    @Max(value = 4, message = "{max.application.grade}")
    private Long grade;

    @NotNull(message = "{notNull.application.birth}")
    private LocalDate birth;

    @NotNull(message = "{notNull.application.address}")
    private String address;

    @NotNull(message = "{notNull.application.interests}")
    private String interests;

    @Size(max = 1000, message = "{size.application.otherActivities}")
    private String otherActivities;

    @URL(message = "{url.application.githubUrl}")
    private String githubUrl;

    @NotNull(message = "{notNull.application.applicationType}")
    private ApplicationType applicationType;

    public static ApplicationRequestDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationRequestDto.class);
    }

}
