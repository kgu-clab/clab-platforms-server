package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginAttemptResult {

    SUCCESS("SUCCESS", "로그인 성공"),
    FAILURE("FAILURE", "로그인 실패");

    private String key;
    private String description;

}
