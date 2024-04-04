package page.clab.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.member.domain.StudentStatus;

import java.time.LocalDate;

@Getter
@Setter
public class MemberUpdateRequestDto {

    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Schema(description = "연락처", example = "01012345678")
    private String contact;

    @Schema(description = "이메일", example = "clab.coreteam@gamil.com")
    private String email;

    @Schema(description = "학년", example = "1")
    private Long grade;

    @Schema(description = "생년월일", example = "2004-01-01")
    private LocalDate birth;

    @Schema(description = "주소", example = "경기도 수원시 영통구 광교산로 154-42")
    private String address;

    @Schema(description = "관심 분야", example = "백엔드")
    private String interests;

    @Schema(description = "GitHub 주소", example = "https://github.com/kgu-c-lab")
    private String githubUrl;

    @Schema(description = "학적", example = "CURRENT")
    private StudentStatus studentStatus;

    @Schema(description = "프로필 이미지", example = "https://www.clab.page/assets/dongmin-860f3a1e.jpeg")
    private String imageUrl;

}
