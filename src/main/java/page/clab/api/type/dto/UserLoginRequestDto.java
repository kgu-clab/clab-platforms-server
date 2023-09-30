package page.clab.api.type.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String id;

    private String password;

}