package page.clab.api.type.dto;

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
    private String username;

    @NotNull(message = "{notNull.sharedAccount.password}")
    @Size(min = 1, message = "{size.sharedAccount.password}")
    private String password;

    @NotNull(message = "{notNull.sharedAccount.platformName}")
    @Size(min = 1, message = "{size.sharedAccount.platformName}")
    private String platformName;

    @NotNull(message = "{notNull.sharedAccount.platformUrl}")
    @URL(message = "{url.sharedAccount.platformUrl}")
    private String platformUrl;

}
