package page.clab.api.domain.sharedAccount.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedAccountUpdateRequestDto {

    @Schema(description = "아이디", example = "clab8510@gmail.com")
    private String username;

    @Schema(description = "비밀번호", example = "Tlfoq8308!")
    private String password;

    @Schema(description = "플랫폼명", example = "인프런")
    private String platformName;

    @Schema(description = "플랫폼 URL", example = "https://www.inflearn.com/")
    private String platformUrl;

}
