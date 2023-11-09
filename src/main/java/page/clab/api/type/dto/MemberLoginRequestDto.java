package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberLoginRequestDto {

    @NotNull
    private String id;

    @NotNull
    private String password;

}