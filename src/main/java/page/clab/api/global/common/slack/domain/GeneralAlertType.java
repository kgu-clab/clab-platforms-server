package page.clab.api.global.common.slack.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralAlertType implements AlertType {

    ADMIN_LOGIN("관리자 로그인", "Admin login."),
    SWAGGER_ACCESS("Swagger 접근", "Swagger accessed."),
    APPLICATION_CREATED("새 지원서", "New application has been submitted."),
    BOARD_CREATED("새 게시글", "New board has been created."),
    SERVER_START("서버 시작", "Server has been started."),
    SERVER_ERROR("서버 에러", "Server error occurred.");

    private final String title;
    private final String defaultMessage;

}