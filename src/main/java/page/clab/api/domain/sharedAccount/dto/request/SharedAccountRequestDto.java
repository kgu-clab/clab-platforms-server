package page.clab.api.domain.sharedAccount.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;

@Getter
@Setter
public class SharedAccountRequestDto {

    @NotNull(message = "{notNull.sharedAccount.username}")
    @Schema(description = "아이디", example = "abc@gmail.com", required = true)
    private String username;

    @NotNull(message = "{notNull.sharedAccount.password}")
    @Schema(description = "비밀번호", example = "1234", required = true)
    private String password;

    @NotNull(message = "{notNull.sharedAccount.platformName}")
    @Schema(description = "플랫폼명", example = "인프런", required = true)
    private String platformName;

    @NotNull(message = "{notNull.sharedAccount.platformUrl}")
    @Schema(description = "플랫폼 URL", example = "https://www.inflearn.com/", required = true)
    private String platformUrl;

    public static SharedAccount toEntity(SharedAccountRequestDto requestDto) {
        return SharedAccount.builder()
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .platformName(requestDto.getPlatformName())
                .platformUrl(requestDto.getPlatformUrl())
                .isInUse(false)
                .build();
    }

}
