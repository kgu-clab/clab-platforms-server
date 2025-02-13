package page.clab.api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST Errors
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "제약 조건 위반이 발생했습니다."),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "HTTP 메시지를 읽을 수 없습니다."),
    ILLEGAL_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근이 감지되었습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 매개변수가 제공되었습니다."),
    INDEX_OUT_OF_BOUNDS(HttpStatus.BAD_REQUEST, "인덱스가 허용된 범위를 벗어났습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리가 포함되었습니다."),
    INVALID_DATA_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 데이터 접근 방식입니다."),
    INVALID_FIELD(HttpStatus.BAD_REQUEST, "유효하지 않은 필드가 포함되었습니다."),
    INVALID_FILE_ATTRIBUTE(HttpStatus.BAD_REQUEST, "파일 속성이 유효하지 않습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "파일 이름이 유효하지 않습니다."),
    INVALID_FILE_PATH(HttpStatus.BAD_REQUEST, "파일 경로가 유효하지 않습니다."),
    INVALID_INFORMATION(HttpStatus.BAD_REQUEST, "유효하지 않은 정보가 포함되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    INVALID_PARENT_BOARD(HttpStatus.BAD_REQUEST, "부모 게시판이 올바르지 않습니다."),
    JWT_SECURITY_ERROR(HttpStatus.BAD_REQUEST, "JWT 토큰에 보안 오류가 있습니다."),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "JSON 형식이 잘못되었습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "메서드 매개변수가 유효하지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 매개변수가 누락되었습니다."),
    NO_SUCH_FIELD(HttpStatus.BAD_REQUEST, "존재하지 않는 필드입니다."),
    NUMBER_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "숫자 형식이 잘못되었습니다."),
    SORTING_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "정렬 기준이 잘못되었습니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "매개변수 유형이 일치하지 않습니다."),
    UNKNOWN_PATH(HttpStatus.BAD_REQUEST, "쿼리의 경로가 잘못되었습니다."),

    // 401 UNAUTHORIZED Errors
    AUTHENTICATION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증 정보를 찾을 수 없습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 인증 정보입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_PRINCIPAL(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다."),
    MALFORMED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰 형식이 잘못되었습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_FORGERY(HttpStatus.UNAUTHORIZED, "토큰 위조가 감지되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자 이름을 찾을 수 없습니다."),

    // 403 FORBIDDEN Errors
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    AUTHORIZATION_DENIED(HttpStatus.FORBIDDEN, "권한이 거부되었습니다."),
    AUTHORIZATION_SERVICE_ERROR(HttpStatus.FORBIDDEN, "권한 서비스 오류가 발생했습니다."),
    FILE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "파일 접근이 거부되었습니다."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 거부되었습니다."),

    // 404 NOT_FOUND Errors
    ELEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 요소를 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 파일을 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // 409 CONFLICT Errors
    BOOK_ALREADY_APPLIED_FOR_LOAN(HttpStatus.CONFLICT, "이미 대출 신청한 도서입니다."),
    ILLEGAL_STATE(HttpStatus.CONFLICT, "잘못된 상태입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "사용자가 이미 존재합니다."),
    MAX_BORROW_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "대출 가능한 도서의 수를 초과했습니다."),

    // 423 LOCKED Errors
    ACCOUNT_LOCKED(HttpStatus.LOCKED, "계정이 잠겼습니다."),

    // 500 INTERNAL_SERVER_ERROR Errors
    ARRAY_INDEX_OUT_OF_BOUNDS(HttpStatus.INTERNAL_SERVER_ERROR, "배열 인덱스가 허용된 범위를 벗어났습니다."),
    COMPLETION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "완료 처리 중 예외가 발생했습니다."),
    DATA_INTEGRITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 무결성 오류가 발생했습니다."),
    DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "복호화 오류가 발생했습니다."),
    DIRECTORY_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "디렉토리 생성에 실패했습니다."),
    ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "암호화 오류가 발생했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
    FILE_PERMISSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 권한 오류가 발생했습니다."),
    IMAGE_COMPRESSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 압축 실패가 발생했습니다."),
    INCORRECT_RESULT_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "결과 크기가 예상과 일치하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "입출력 오류가 발생했습니다."),
    RHYTHM_GENERATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "리듬 생성 중 오류가 발생했습니다."),
    SECURITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "보안 오류가 발생했습니다."),
    SQL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SQL 처리 중 오류가 발생했습니다."),
    TRANSACTION_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 시스템 오류가 발생했습니다."),
    UNEXPECTED_ROLLBACK(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션이 예기치 않게 롤백되었습니다."),
    OPTIMISTIC_LOCKING_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "동시에 다른 사용자가 대출 신청하여 충돌이 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
