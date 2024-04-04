package page.clab.api.domain.login.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenInfo {

    private String accessToken;

    private String refreshToken;

}