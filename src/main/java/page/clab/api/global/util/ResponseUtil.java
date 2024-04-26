package page.clab.api.global.util;

import jakarta.servlet.http.HttpServletResponse;
import page.clab.api.global.common.dto.ApiResponse;

import java.io.IOException;

public class ResponseUtil {

    public static void sendErrorResponse(HttpServletResponse response, int status) throws IOException {
        response.getWriter().write(ApiResponse.failure().toJson());
        response.setContentType("application/json");
        response.setStatus(status);
    }

}
