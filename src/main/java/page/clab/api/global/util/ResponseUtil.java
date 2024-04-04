package page.clab.api.global.util;

import jakarta.servlet.http.HttpServletResponse;
import page.clab.api.global.common.dto.ResponseModel;

import java.io.IOException;

public class ResponseUtil {

    public static void sendErrorResponse(HttpServletResponse response, int status) throws IOException {
        response.getWriter().write(ResponseModel.failure().toJson());
        response.setContentType("application/json");
        response.setStatus(status);
    }

}
