package page.clab.api.global.common.slack.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityAlertType {

    ABNORMAL_ACCESS("비정상적인 접근", "Unexpected access pattern detected."),
    REPEATED_LOGIN_FAILURES("지속된 로그인 실패", "Multiple consecutive failed login attempts.");

    private final String title;
    private final String defaultMessage;

}
