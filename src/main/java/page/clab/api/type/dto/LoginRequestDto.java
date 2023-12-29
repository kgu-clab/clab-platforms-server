package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequestDto {

    @NotNull(message = "{notNull.login.id}")
    @Size(min = 1, message = "{size.login.id}")
    @Schema(description = "학번", example = "202312000", required = true)
    private String id;

    @NotNull(message = "{notNull.login.password}")
    @Size(min = 1, message = "{size.login.password}")
    @Schema(description = "비밀번호", example = "1234", required = true)
    private String password;

}