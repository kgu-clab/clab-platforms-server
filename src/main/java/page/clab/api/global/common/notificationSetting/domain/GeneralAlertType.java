package page.clab.api.global.common.notificationSetting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralAlertType implements AlertType {

    ADMIN_LOGIN("관리자 로그인", "Admin login."),
    SERVER_START("서버 시작", "Server has been started."),
    SERVER_ERROR("서버 에러", "Server error occurred.");

    private final String title;
    private final String defaultMessage;
}
