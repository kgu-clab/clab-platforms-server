package page.clab.api.global.auth.domain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClabAuthResponseStatus {

    AUTHENTICATION_SUCCESS(Integer.parseInt("1" + HttpServletResponse.SC_OK), "인증 성공");

    private final int httpStatus;

    private final String message;

}
