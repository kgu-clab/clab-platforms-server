package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotNull(message = "{notNull.login.id}")
    @Size(min = 1, message = "{size.login.id}")
    private String id;

    @NotNull(message = "{notNull.login.password}")
    @Size(min = 1, message = "{size.login.password}")
    private String password;

}