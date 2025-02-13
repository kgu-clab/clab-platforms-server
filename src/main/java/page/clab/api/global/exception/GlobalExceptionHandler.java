package page.clab.api.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import page.clab.api.global.common.dto.ErrorResponse;

@RestControllerAdvice(basePackages = "page.clab.api")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ErrorResponse<Exception> handleBaseException(HttpServletResponse response, BaseException ex) {
        response.setStatus(ex.getErrorCode().getStatus().value());
        return ErrorResponse.failure(ex);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse<Exception> handleServerError(HttpServletRequest request, HttpServletResponse response,
        Exception ex) {
        ErrorCode errorCode = ExceptionMapper.getErrorCode(ex);
        response.setStatus(errorCode.getStatus().value());
        log.error("Server error occurred.", ex);
        log.error("Error message: {}", ex.getMessage());
        return ErrorResponse.failure(new BaseException(errorCode, ex.getMessage()));
    }
}
