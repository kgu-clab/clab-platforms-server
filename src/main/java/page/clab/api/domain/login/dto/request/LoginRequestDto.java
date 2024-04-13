package page.clab.api.domain.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotNull(message = "{notNull.login.id}")
    @Schema(description = "학번", example = "202312000", required = true)
    private String id;

    @NotNull(message = "{notNull.login.password}")
    @Schema(description = "비밀번호", example = "1234", required = true)
    private String password;

}