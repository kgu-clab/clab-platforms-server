package page.clab.api.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import page.clab.api.global.common.dto.ErrorResponse;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.GeneralAlertType;

@RestControllerAdvice(basePackages = "page.clab.api")
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler(BaseException.class)
    public ErrorResponse<Exception> handleBaseException(
        HttpServletResponse response,
        BaseException e
    ) {
        response.setStatus(e.getErrorCode().getStatus().value());
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse<Exception> handleServerError(
        HttpServletRequest request,
        HttpServletResponse response,
        Exception e
    ) {
        ErrorCode errorCode = ExceptionMapper.getErrorCode(e);
        HttpStatus status = errorCode.getStatus();

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            eventPublisher.publishEvent(
                new NotificationEvent(this, GeneralAlertType.SERVER_ERROR, request, e));
            log.warn(e.getMessage());
        }

        response.setStatus(status.value());
        return ErrorResponse.failure(new BaseException(errorCode, e.getMessage()));
    }
}
