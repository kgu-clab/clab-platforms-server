package page.clab.api.domain.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorAuthenticationRequestDto {

    @NotNull(message = "{notNull.twoFactorAuthenticationRequestDto.memberId}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String memberId;

    @NotNull(message = "{notNull.twoFactorAuthenticationRequestDto.totp}")
    @Schema(description = "TOTP", example = "123456", required = true)
    private String totp;

}
