package page.clab.api.global.common.email.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateType {

    ACCOUNT_CREATION("account-creation", "계정 생성 템플릿", "clabEmail.html"),
    PASSWORD_RESET("password-reset", "비밀번호 재설정 템플릿", "clabEmail.html");

    private final String key;
    private final String description;
    private final String templateName;
}
