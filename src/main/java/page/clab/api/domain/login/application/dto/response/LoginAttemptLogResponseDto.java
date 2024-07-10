package page.clab.api.domain.login.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.domain.LoginAttemptResult;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoginAttemptLogResponseDto {

    private Long id;
    private String userAgent;
    private String ipAddress;
    private String location;
    private LoginAttemptResult loginAttemptResult;
    private LocalDateTime loginAttemptTime;

    public static LoginAttemptLogResponseDto toDto(LoginAttemptLog loginAttemptLog) {
        return LoginAttemptLogResponseDto.builder()
                .id(loginAttemptLog.getId())
                .userAgent(loginAttemptLog.getUserAgent())
                .ipAddress(loginAttemptLog.getIpAddress())
                .location(loginAttemptLog.getLocation())
                .loginAttemptResult(loginAttemptLog.getLoginAttemptResult())
                .loginAttemptTime(loginAttemptLog.getLoginAttemptTime())
                .build();
    }
}
