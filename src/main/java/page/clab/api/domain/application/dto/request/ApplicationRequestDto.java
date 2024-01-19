package page.clab.api.domain.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequestDto {

    @NotNull(message = "{notNull.application.studentId}")
    @Size(min = 9, max = 9, message = "{size.application.studentId}")
    @Pattern(regexp = "^[0-9]+$", message = "{pattern.application.studentId}")
    @Schema(description = "학번", example = "202312000", required = true)
    private String studentId;

    @NotNull(message = "{notNull.application.name}")
    @Size(min = 1, max = 10, message = "{size.application.name}")
    @Schema(description = "이름", example = "홍길동", required = true)
    private String name;

    @NotNull(message = "{notNull.application.contact}")
    @Size(min = 11, max = 11, message = "{size.application.contact}")
    @Pattern(regexp = "^[0-9]+$", message = "{pattern.application.contact}")
    @Schema(description = "연락처", example = "01012345678", required = true)
    private String contact;

    @NotNull(message = "{notNull.application.email}")
    @Email(message = "{email.application.email}")
    @Size(min = 1, message = "{size.application.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gamil.com", required = true)
    private String email;

    @NotNull(message = "{notNull.application.department}")
    @Size(min = 1, message = "{size.application.department}")
    @Schema(description = "학과", example = "AI컴퓨터공학부", required = true)
    private String department;

    @NotNull(message = "{notNull.application.grade}")
    @Min(value = 1, message = "{min.application.grade}")
    @Max(value = 4, message = "{max.application.grade}")
    @Schema(description = "학년", example = "1", required = true)
    private Long grade;

    @NotNull(message = "{notNull.application.birth}")
    @Schema(description = "생년월일", example = "2004-01-01", required = true)
    private LocalDate birth;

    @NotNull(message = "{notNull.application.address}")
    @Size(min = 1, message = "{size.application.address}")
    @Schema(description = "주소", example = "경기도 수원시 영통구 광교산로 154-42", required = true)
    private String address;

    @NotNull(message = "{notNull.application.interests}")
    @Schema(description = "관심분야", example = "백엔드", required = true)
    private String interests;

    @Size(max = 1000, message = "{size.application.otherActivities}")
    @Schema(description = "IT 관련 자격증 및 활동", example = "경기대학교 컴퓨터공학과 학생회")
    private String otherActivities;

    @URL(message = "{url.application.githubUrl}")
    @Schema(description = "GitHub", example = "https://github.com/KGU-C-Lab")
    private String githubUrl;

    @NotNull(message = "{notNull.application.applicationType}")
    @Schema(description = "구분", example = "NORMAL", required = true)
    private ApplicationType applicationType;

    public static ApplicationRequestDto of(Application application) {
        return ModelMapperUtil.getModelMapper().map(application, ApplicationRequestDto.class);
    }

}
