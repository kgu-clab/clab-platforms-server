package page.clab.api.global.verificationCode.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class VerificationCodeRequestDto {

    @NotNull(message = "{notNull.verificationCode.memberId}")
    @Size(min = 9, max = 9, message = "{size.verificationCode.memberId}")
    @Schema(description = "학번", example = "202310000", required = true)
    private String memberId;

    @NotNull(message = "{notNull.verificationCode.verificationCode}")
    @Size(min = 12, max = 12, message = "{size.verificationCode.verificationCode}")
    @Schema(description = "인증 코드", example = "123456789012", required = true)
    private String verificationCode;

}
