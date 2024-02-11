package page.clab.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.StudentStatus;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequestDto {

    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Size(min = 11, max = 11, message = "{size.member.contact}")
    @Schema(description = "연락처", example = "01012345678")
    private String contact;

    @Email(message = "{email.member.email}")
    @Size(min = 1, message = "{size.member.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gamil.com")
    private String email;

    @Min(value = 1, message = "{min.member.grade}")
    @Max(value = 4, message = "{max.member.grade}")
    @Schema(description = "학년", example = "1")
    private Long grade;

    @Schema(description = "생년월일", example = "2004-01-01")
    private LocalDate birth;

    @Size(min = 1, message = "{size.member.address}")
    @Schema(description = "주소", example = "경기도 수원시 영통구 광교산로 154-42")
    private String address;

    @Size(min = 1, message = "{size.member.interests}")
    @Schema(description = "관심 분야", example = "백엔드")
    private String interests;

    @URL(message = "{url.member.githubUrl}")
    @Schema(description = "GitHub 주소", example = "https://github.com/kgu-c-lab")
    private String githubUrl;

    @Schema(description = "학적", example = "CURRENT")
    private StudentStatus studentStatus;

    @URL(message = "{url.member.imageUrl}")
    @Schema(description = "프로필 이미지", example = "https://www.clab.page/assets/dongmin-860f3a1e.jpeg")
    private String imageUrl;

    public static MemberUpdateRequestDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberUpdateRequestDto.class);
    }

}
