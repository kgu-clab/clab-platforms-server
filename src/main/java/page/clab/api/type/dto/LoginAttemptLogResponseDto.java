package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.etc.LoginAttemptResult;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginAttemptLogResponseDto {

    private Long id;

    private String userAgent;

    private String ipAddress;

    private String location;

    private LoginAttemptResult loginAttemptResult;

    private LocalDateTime loginAttemptTime;

    public static LoginAttemptLogResponseDto of(LoginAttemptLog loginAttemptLog) {
        return ModelMapperUtil.getModelMapper().map(loginAttemptLog, LoginAttemptLogResponseDto.class);
    }

}
