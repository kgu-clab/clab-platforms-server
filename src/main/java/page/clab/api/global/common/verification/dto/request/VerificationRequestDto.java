package page.clab.api.global.common.verification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class VerificationRequestDto {

    @NotNull(message = "{notNull.verificationCode.memberId}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String memberId;

    @NotNull(message = "{notNull.verificationCode.verificationCode}")
    @Schema(description = "인증 코드", example = "123456789012", required = true)
    private String verificationCode;

}
