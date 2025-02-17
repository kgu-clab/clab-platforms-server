package page.clab.api.global.exception;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    // 커스텀 메시지를 전달하지 않을 경우, ErrorCode의 기본 메시지 사용
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    // 커스텀 메시지를 전달할 경우, 해당 메시지 사용
    public BaseException(ErrorCode errorCode, String customMessage) {
        super(StringUtils.isNotEmpty(customMessage) ? customMessage : errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
}
