package page.clab.api.domain.sharedAccount.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedAccountUpdateRequestDto {

    @Size(min = 1, message = "{size.sharedAccount.username}")
    @Schema(description = "아이디", example = "clab8510@gmail.com")
    private String username;

    @Size(min = 1, message = "{size.sharedAccount.password}")
    @Schema(description = "비밀번호", example = "Tlfoq8308!")
    private String password;

    @Size(min = 1, message = "{size.sharedAccount.platformName}")
    @Schema(description = "플랫폼명", example = "인프런")
    private String platformName;

    @URL(message = "{url.sharedAccount.platformUrl}")
    @Schema(description = "플랫폼 URL", example = "https://www.inflearn.com/")
    private String platformUrl;

}
