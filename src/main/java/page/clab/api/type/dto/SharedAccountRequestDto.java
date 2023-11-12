package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class SharedAccountRequestDto {

    @NotNull(message = "{notNull.sharedAccount.username}")
    @Size(min = 1, message = "{size.sharedAccount.username}")
    @Schema(description = "아이디", example = "clab8510@gmail.com", required = true)
    private String username;

    @NotNull(message = "{notNull.sharedAccount.password}")
    @Size(min = 1, message = "{size.sharedAccount.password}")
    @Schema(description = "비밀번호", example = "Tlfoq8308!", required = true)
    private String password;

    @NotNull(message = "{notNull.sharedAccount.platformName}")
    @Size(min = 1, message = "{size.sharedAccount.platformName}")
    @Schema(description = "플랫폼명", example = "인프런", required = true)
    private String platformName;

    @NotNull(message = "{notNull.sharedAccount.platformUrl}")
    @URL(message = "{url.sharedAccount.platformUrl}")
    @Schema(description = "플랫폼 URL", example = "https://www.inflearn.com/", required = true)
    private String platformUrl;

}
