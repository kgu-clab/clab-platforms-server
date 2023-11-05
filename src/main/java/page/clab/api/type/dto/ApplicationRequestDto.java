package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Application;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequestDto {

    @Size(min = 9, max = 9)
    @Pattern(regexp = "^[0-9]+$")
    private String studentId;

    @NotNull
    @Size(max = 10)
    private String name;

    @NotNull
    @Size(max = 11)
    private String contact;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String department;

    @NotNull
    @Min(1)
    @Max(4)
    private Long grade;

    @NotNull
    private LocalDate birth;

    @NotNull
    private String address;

    @NotNull
    private String interests;

    @Size(max = 1000)
    private String otherActivities;

    @NotNull
    private String githubUrl;

    @NotNull
    private ApplicationType applicationType;

    public static ApplicationRequestDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationRequestDto.class);
    }

}
