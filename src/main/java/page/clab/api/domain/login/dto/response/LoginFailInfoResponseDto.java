package page.clab.api.domain.login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.login.domain.LoginFailInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginFailInfoResponseDto {

    private String id;

    private String name;

    public static LoginFailInfoResponseDto of(LoginFailInfo loginFailInfo) {
        return LoginFailInfoResponseDto.builder()
                .id(loginFailInfo.getMember().getId())
                .name(loginFailInfo.getMember().getName())
                .build();
    }

}
