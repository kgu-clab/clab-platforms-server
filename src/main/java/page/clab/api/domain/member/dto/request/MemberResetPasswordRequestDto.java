package page.clab.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResetPasswordRequestDto {

    @NotNull(message = "{notNull.member.id}")
    @Size(min = 9, max = 9, message = "{size.member.id}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String id;
    
    @NotNull(message = "{notNull.member.name}")
    @Size(min = 1, max = 10, message = "{size.member.name}")
    @Schema(description = "이름", example = "홍길동", required = true)
    private String name;

    @NotNull(message = "{notNull.member.email}")
    @Email(message = "{email.member.email}")
    @Size(min = 1, message = "{size.member.email}")
    @Schema(description = "이메일", example = "clab.coreteam@gamil.com", required = true)
    private String email;

}
