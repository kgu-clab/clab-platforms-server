package page.clab.api.domain.login.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginAttemptResult {

    TOTP("TOTP", "2FA 인증 성공"),
    SUCCESS("SUCCESS", "로그인 성공"),
    FAILURE("FAILURE", "로그인 실패");

    private String key;
    private String description;
}
