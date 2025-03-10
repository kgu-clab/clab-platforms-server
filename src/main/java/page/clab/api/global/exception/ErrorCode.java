package page.clab.api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode 열거형은 애플리케이션 전반에서 발생하는 예외에 대해 일관된 에러 코드를 정의합니다.
 *
 * <p>
 * 프론트엔드에서 상태 코드가 4xx일 경우 응답 본문(body)을 읽지 못하는 이슈가 있어, 다음과 같이 처리합니다.
 * <ul>
 *   <li>
 *     401(UNAUTHORIZED), 403(FORBIDDEN), 500(INTERNAL_SERVER_ERROR) 에 해당하는 에러는
 *     해당 HTTP 상태 코드를 그대로 사용합니다.
 *   </li>
 *   <li>
 *     400(BAD_REQUEST) 그룹의 예외 중 {@code MethodArgumentNotValidException} 및
 *     {@code ConstraintViolationException}에 해당하는 경우에만 400 상태 코드를 사용하며,
 *     그 외의 400 에러는 클라이언트가 응답 본문을 정상적으로 읽을 수 있도록 200 OK 상태 코드로 처리합니다.
 *   </li>
 *   <li>
 *     404(NOT_FOUND) 그룹의 에러 역시 리소스나 요소를 찾지 못한 경우로, 클라이언트가 응답 본문을 정상적으로 읽을 수 있도록 200 OK 상태 코드로 처리됩니다.
 *   </li>
 *   <li>
 *     409(CONFLICT) 그룹의 에러도 마찬가지로 클라이언트에서 응답 본문을 정상적으로 읽을 수 있도록 200 OK 상태 코드로 처리합니다.
 *   </li>
 * </ul>
 * </p>
 *
 * <p>
 * 이러한 방식은 클라이언트가 에러 응답의 본문을 정상적으로 파싱할 수 있도록 지원하며,
 * {@code errorMessage} 필드에 {@link ErrorCode} 이름을 전달하여 클라이언트가 오류를 쉽게 식별하고 처리할 수 있도록 합니다.
 * </p>
 */
@Getter
public enum ErrorCode {

    /**
     * 400 BAD_REQUEST Errors
     * <p>
     * {@code MethodArgumentNotValidException} 및 {@code ConstraintViolationException}에 해당하는 경우에만 400 상태 코드를 사용합니다. 그 외의
     * 400 그룹 에러는 클라이언트가 응답 본문을 정상적으로 읽을 수 있도록 200 OK 상태 코드로 처리됩니다.
     * </p>
     */
    ALERT_TYPE_NOT_FOUND(HttpStatus.OK, "알림 타입을 찾을 수 없습니다."),
    ASSIGNMENT_BOARD_HAS_NO_DUE_DATE(HttpStatus.OK, "과제 게시판에 마감 기한이 없습니다."),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "제약 조건 위반이 발생했습니다."),
    EXECUTIVE_ALREADY_REGISTERED(HttpStatus.OK, "이미 등록된 운영진입니다."),
    EXECUTIVE_NOT_A_MEMBER(HttpStatus.OK, "운영진 등록 대상은 멤버여야 합니다."),
    FEEDBACK_BOARD_HAS_NO_CONTENT(HttpStatus.OK, "피드백 게시판의 내용이 비어있습니다."),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.OK, "HTTP 메시지를 읽을 수 없습니다."),
    ILLEGAL_ACCESS(HttpStatus.OK, "잘못된 접근이 감지되었습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.OK, "유효하지 않은 매개변수가 제공되었습니다."),
    INACTIVE_ACTIVITY_GROUP_MEMBER(HttpStatus.OK, "활동에 참여하지 않은 멤버의 직책을 변경할 수 없습니다."),
    INDEX_OUT_OF_BOUNDS(HttpStatus.OK, "인덱스가 허용된 범위를 벗어났습니다."),
    INVALID_BOARD_CATEGORY_HASHTAG(HttpStatus.OK, "해당 게시판에 해시태그를 사용할 수 없습니다."),
    INVALID_COLUMN(HttpStatus.OK, "유효하지 않은 열이 포함되었습니다."),
    INVALID_DATA_ACCESS(HttpStatus.OK, "잘못된 데이터 접근 방식입니다."),
    INVALID_DATE_RANGE(HttpStatus.OK, "시작 날짜는 종료 날짜 이후일 수 없습니다."),
    INVALID_DATE_TIME_RANGE(HttpStatus.OK, "시작 일시는 종료 일시 이후일 수 없습니다."),
    INVALID_FILE_ATTRIBUTE(HttpStatus.OK, "파일 속성이 유효하지 않습니다."),
    INVALID_GITHUB_URL(HttpStatus.OK, "유효하지 않은 GitHub URL입니다."),
    INVALID_PARENT_BOARD(HttpStatus.OK, "부모 게시판이 올바르지 않습니다."),
    INVALID_RECRUITMENT_CLOSURE_WINDOW(HttpStatus.OK, "모집 종료일이 현재 날짜 기준 7일을 초과했거나, 아직 모집이 종료되지 않았습니다."),
    INVALID_ROLE_CHANGE(HttpStatus.OK, "유효하지 않은 권한 변경입니다."),
    INVALID_SORT_DIRECTION(HttpStatus.OK, "지원하지 않는 정렬 방식입니다."),
    INVALID_VERIFICATION_REQUEST(HttpStatus.OK, "인증 요청 정보가 올바르지 않습니다."),
    JWT_SECURITY_ERROR(HttpStatus.OK, "JWT 토큰에 보안 오류가 있습니다."),
    MALFORMED_JSON(HttpStatus.OK, "JSON 형식이 잘못되었습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "메서드 매개변수가 유효하지 않습니다."),
    MISSING_PARAMETER(HttpStatus.OK, "필수 매개변수가 누락되었습니다."),
    NO_SUCH_FIELD(HttpStatus.OK, "존재하지 않는 필드입니다."),
    NUMBER_FORMAT_ERROR(HttpStatus.OK, "숫자 형식이 잘못되었습니다."),
    RECRUITMENT_NOT_ACTIVE(HttpStatus.OK, "모집 공고가 활성 상태가 아닙니다."),
    RESET_PASSWORD_INFORMATION_MISMATCH(HttpStatus.OK, "비밀번호 재설정을 위한 회원 정보가 일치하지 않습니다."),
    SORTING_ARGUMENT_MISMATCH(HttpStatus.OK, "정렬 기준과 정렬 순서의 개수가 일치하지 않습니다."),
    TYPE_MISMATCH(HttpStatus.OK, "매개변수 유형이 일치하지 않습니다."),
    UNKNOWN_PATH(HttpStatus.OK, "쿼리의 경로가 잘못되었습니다."),
    UNSUPPORTED_EMOJI(HttpStatus.OK, "지원하지 않는 이모지입니다."),
    UNSUPPORTED_OPERATION(HttpStatus.OK, "지원되지 않는 작업입니다."),

    /**
     * 401 UNAUTHORIZED Errors
     */
    AUTHENTICATION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증 정보가 존재하지 않습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 인증 정보입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    MALFORMED_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰 형식이 잘못되었습니다."),
    MEMBER_LOCKED(HttpStatus.UNAUTHORIZED, "정책에 의해 계정이 일시적으로 잠겼습니다."),
    TOKEN_FORGERY(HttpStatus.UNAUTHORIZED, "토큰 위조가 감지되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MISUSED(HttpStatus.UNAUTHORIZED, "토큰이 오용되었습니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자 이름을 찾을 수 없습니다."),

    /**
     * 403 FORBIDDEN Errors
     */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    AUTHORIZATION_DENIED(HttpStatus.FORBIDDEN, "권한이 거부되었습니다."),
    AUTHORIZATION_SERVICE_ERROR(HttpStatus.FORBIDDEN, "권한 서비스 오류가 발생했습니다."),
    FILE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "파일 접근이 거부되었습니다."),
    MEMBER_NOT_PART_OF_ACTIVITY_GROUP(HttpStatus.FORBIDDEN, "활동 그룹에 속하지 않은 멤버입니다."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 거부되었습니다."),

    /**
     * 404 NOT_FOUND Errors
     * <p>
     * 해당 에러는 리소스 또는 요소를 찾지 못한 경우에 사용되며, 클라이언트가 응답 본문을 정상적으로 읽을 수 있도록 200 OK 상태 코드로 처리됩니다.
     * </p>
     */
    NOT_FOUND(HttpStatus.OK, "요청한 정보를 찾을 수 없습니다."),
    ELEMENT_NOT_FOUND(HttpStatus.OK, "요청한 요소를 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.OK, "요청한 파일을 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.OK, "요청한 리소스를 찾을 수 없습니다."),
    INVALID_PATH_VARIABLE(HttpStatus.OK, "유효하지 않은 경로 변수입니다."),

    /**
     * 409 CONFLICT Errors
     * <p>
     * 클라이언트가 응답 본문을 정상적으로 읽을 수 있도록, 409 에러 그룹은 200 OK 상태 코드로 처리됩니다.
     * </p>
     */
    ACCUSE_TARGET_TYPE_MISMATCH(HttpStatus.OK, "신고 대상의 유형이 일치하지 않습니다."),
    ACTIVITY_GROUP_NOT_FINISHED(HttpStatus.OK, "활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다."),
    ACTIVITY_GROUP_NOT_PROGRESSING(HttpStatus.OK, "해당 활동은 진행중인 활동이 아닙니다."),
    ALREADY_APPLIED_ACTIVITY_GROUP(HttpStatus.OK, "이미 신청한 활동 그룹입니다."),
    ALREADY_HAS_SAME_ACTIVITY_GROUP_ROLE(HttpStatus.OK, "이미 해당 직책을 가지고 있습니다."),
    ALREADY_REVIEWED(HttpStatus.OK, "이미 리뷰를 작성한 활동 그룹입니다."),
    ALREADY_SUBMITTED_THIS_WEEK_ASSIGNMENT(HttpStatus.OK, "해당 주차의 과제물이 이미 제출되었습니다."),
    BOOK_ALREADY_APPLIED_FOR_LOAN(HttpStatus.OK, "이미 대출 신청한 도서입니다."),
    BOOK_ALREADY_BORROWED(HttpStatus.OK, "이미 대출 중인 도서입니다."),
    BOOK_ALREADY_RETURNED(HttpStatus.OK, "이미 반납된 도서입니다."),
    BOOK_BORROWER_MISMATCH(HttpStatus.OK, "대출한 도서와 회원 정보가 일치하지 않습니다."),
    BOOK_BORROWING_NOT_ALLOWED_DURING_SUSPENSION(HttpStatus.OK, "대출 정지 중입니다. 대출 정지일까지는 책을 대출할 수 없습니다."),
    BOOK_LOAN_EXTENSION_LIMIT_EXCEEDED(HttpStatus.OK, "대출 연장 횟수를 초과했습니다."),
    BOOK_LOAN_STATUS_NOT_PENDING(HttpStatus.OK, "대출 신청 상태가 아닙니다."),
    DUPLICATE_ABSENT_EXCUSE(HttpStatus.OK, "이미 해당 결석에 대해 불참 사유서가 등록되어 있습니다."),
    DUPLICATE_ATTENDANCE(HttpStatus.OK, "이미 해당 날짜에 출석 정보가 등록되어 있습니다."),
    DUPLICATE_MEMBER_CONTACT(HttpStatus.OK, "이미 해당 연락처로 가입된 멤버가 존재합니다."),
    DUPLICATE_MEMBER_EMAIL(HttpStatus.OK, "이미 해당 이메일로 가입된 멤버가 존재합니다."),
    DUPLICATE_MEMBER_ID(HttpStatus.OK, "이미 해당 ID로 가입된 멤버가 존재합니다."),
    DUPLICATE_REPORT(HttpStatus.OK, "이미 해당 차시의 보고서가 존재합니다."),
    ILLEGAL_STATE(HttpStatus.OK, "잘못된 상태입니다."),
    INSUFFICIENT_CLOUD_STORAGE(HttpStatus.OK, "클라우드 저장 공간이 부족합니다."),
    LEADER_STATUS_CHANGE_NOT_ALLOWED(HttpStatus.OK, "리더의 상태는 변경할 수 없습니다."),
    MAX_BORROW_LIMIT_EXCEEDED(HttpStatus.OK, "대출 가능한 도서의 수를 초과했습니다."),
    NOT_APPROVED_APPLICATION(HttpStatus.OK, "승인되지 않은 지원서입니다."),
    OVERDUE_BOOK_EXTENSION_NOT_ALLOWED(HttpStatus.OK, "연체 중인 도서는 연장할 수 없습니다."),
    SINGLE_LEADER_MODIFICATION_NOT_ALLOWED(HttpStatus.OK, "그룹에는 최소 한 명의 리더가 있어야 하므로, 리더의 역할을 변경할 수 없습니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR Errors
     */
    ARRAY_INDEX_OUT_OF_BOUNDS(HttpStatus.INTERNAL_SERVER_ERROR, "배열 인덱스가 허용된 범위를 벗어났습니다."),
    COMPLETION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "완료 처리 중 예외가 발생했습니다."),
    DATA_INTEGRITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 무결성 오류가 발생했습니다."),
    DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "복호화 오류가 발생했습니다."),
    DIRECTORY_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "디렉토리 생성에 실패했습니다."),
    ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "암호화 오류가 발생했습니다."),
    FILE_PERMISSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 권한 오류가 발생했습니다."),
    IMAGE_COMPRESSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 압축에 실패했습니다."),
    IMAGE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 중 오류가 발생했습니다."),
    INCORRECT_RESULT_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "결과 크기가 예상과 일치하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "입출력 오류가 발생했습니다."),
    MESSAGING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "메시징 처리 중 오류가 발생했습니다."),
    METADATA_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "메타데이터 처리 중 오류가 발생했습니다."),
    OPTIMISTIC_LOCKING_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "동시에 다른 사용자가 대출 신청하여 충돌이 발생했습니다."),
    SECURITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "보안 오류가 발생했습니다."),
    SQL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SQL 처리 중 오류가 발생했습니다."),
    TRANSACTION_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 시스템 오류가 발생했습니다."),
    UNEXPECTED_ROLLBACK(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션이 예기치 않게 롤백되었습니다."),
    WEB_CLIENT_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "웹 클라이언트 요청 중 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
