package page.clab.api.global.util;

import jakarta.servlet.http.HttpServletResponse;
import page.clab.api.global.common.dto.ApiResponse;

import java.io.IOException;

/**
 * {@code ResponseUtil} 클래스는 HTTP 응답에 대한 유틸리티 메서드를 제공합니다.
 * 주로 오류 응답을 JSON 형식으로 전송하는 메서드를 포함하고 있습니다.
 */
public class ResponseUtil {

    public static void sendErrorResponse(HttpServletResponse response, int status) throws IOException {
        response.getWriter().write(ApiResponse.failure().toJson());
        response.setContentType("application/json");
        response.setStatus(status);
    }
}
