package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class TwoFactorAuthenticationRequestDto {

    @NotNull(message = "{notNull.twoFactorAuthenticationRequestDto.memberId}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String memberId;

    @NotNull(message = "{notNull.twoFactorAuthenticationRequestDto.totp}")
    @Schema(description = "TOTP", example = "123456", required = true)
    private String totp;

}
