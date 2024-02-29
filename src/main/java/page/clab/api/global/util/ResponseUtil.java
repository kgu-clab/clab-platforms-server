package page.clab.api.global.util;

import jakarta.servlet.http.HttpServletResponse;
import page.clab.api.global.common.dto.ResponseModel;

import java.io.IOException;

public class ResponseUtil {

    public static void sendErrorResponse(HttpServletResponse response, int status) throws IOException {
        ResponseModel responseModel = page.clab.api.global.common.dto.ResponseModel.builder()
                .success(false)
                .build();
        response.getWriter().write(responseModel.toJson());
        response.setContentType("application/json");
        response.setStatus(status);
    }

    public static ResponseModel createErrorResponse(boolean success, Object message) {
        return ResponseModel.builder()
                .success(success)
                .data(message)
                .build();
    }

}
