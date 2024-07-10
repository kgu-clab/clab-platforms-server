package page.clab.api.domain.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResetPasswordRequestDto {

    @NotNull(message = "{notNull.member.id}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String id;

    @NotNull(message = "{notNull.member.name}")
    @Schema(description = "이름", example = "홍길동", required = true)
    private String name;

    @NotNull(message = "{notNull.member.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gamil.com", required = true)
    private String email;
}
