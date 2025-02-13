package page.clab.api.global.exception;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.google.gson.stream.MalformedJsonException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;
import org.hibernate.query.sqm.UnknownPathException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

public class ExceptionMapper {

    private static final Map<Class<? extends Exception>, ErrorCode> exceptionToErrorCodeMap = new HashMap<>();

    static {
        // 400 BAD_REQUEST Errors
        exceptionToErrorCodeMap.put(ConstraintViolationException.class, ErrorCode.CONSTRAINT_VIOLATION);
        exceptionToErrorCodeMap.put(HttpMessageNotReadableException.class, ErrorCode.HTTP_MESSAGE_NOT_READABLE);
        exceptionToErrorCodeMap.put(IllegalAccessException.class, ErrorCode.ILLEGAL_ACCESS);
        exceptionToErrorCodeMap.put(IllegalArgumentException.class, ErrorCode.ILLEGAL_ARGUMENT);
        exceptionToErrorCodeMap.put(InvalidDataAccessApiUsageException.class, ErrorCode.INVALID_DATA_ACCESS);
        exceptionToErrorCodeMap.put(MalformedJsonException.class, ErrorCode.MALFORMED_JSON);
        exceptionToErrorCodeMap.put(MethodArgumentNotValidException.class, ErrorCode.METHOD_ARGUMENT_NOT_VALID);
        exceptionToErrorCodeMap.put(MethodArgumentTypeMismatchException.class, ErrorCode.TYPE_MISMATCH);
        exceptionToErrorCodeMap.put(MissingServletRequestParameterException.class, ErrorCode.MISSING_PARAMETER);
        exceptionToErrorCodeMap.put(NoSuchFieldException.class, ErrorCode.NO_SUCH_FIELD);
        exceptionToErrorCodeMap.put(NumberFormatException.class, ErrorCode.NUMBER_FORMAT_ERROR);
        exceptionToErrorCodeMap.put(StringIndexOutOfBoundsException.class, ErrorCode.INDEX_OUT_OF_BOUNDS);
        exceptionToErrorCodeMap.put(UnknownPathException.class, ErrorCode.UNKNOWN_PATH);
        exceptionToErrorCodeMap.put(io.jsonwebtoken.security.SecurityException.class, ErrorCode.JWT_SECURITY_ERROR);

        // 401 UNAUTHORIZED Errors
        exceptionToErrorCodeMap.put(AuthenticationException.class, ErrorCode.AUTHENTICATION_NOT_FOUND);
        exceptionToErrorCodeMap.put(BadCredentialsException.class, ErrorCode.BAD_CREDENTIALS);
        exceptionToErrorCodeMap.put(ExpiredJwtException.class, ErrorCode.EXPIRED_JWT);
        exceptionToErrorCodeMap.put(MalformedJwtException.class, ErrorCode.MALFORMED_JWT);
        exceptionToErrorCodeMap.put(UnsupportedJwtException.class, ErrorCode.UNSUPPORTED_JWT);
        exceptionToErrorCodeMap.put(UsernameNotFoundException.class, ErrorCode.USERNAME_NOT_FOUND);

        // 403 FORBIDDEN Errors
        exceptionToErrorCodeMap.put(AccessDeniedException.class, ErrorCode.ACCESS_DENIED);
        exceptionToErrorCodeMap.put(java.nio.file.AccessDeniedException.class, ErrorCode.FILE_ACCESS_DENIED);
        exceptionToErrorCodeMap.put(AuthorizationDeniedException.class, ErrorCode.AUTHORIZATION_DENIED);
        exceptionToErrorCodeMap.put(AuthorizationServiceException.class, ErrorCode.AUTHORIZATION_SERVICE_ERROR);

        // 404 NOT_FOUND Errors
        exceptionToErrorCodeMap.put(FileNotFoundException.class, ErrorCode.FILE_NOT_FOUND);
        exceptionToErrorCodeMap.put(NoSuchElementException.class, ErrorCode.ELEMENT_NOT_FOUND);
        exceptionToErrorCodeMap.put(NotFoundException.class, ErrorCode.RESOURCE_NOT_FOUND);
        exceptionToErrorCodeMap.put(NullPointerException.class, ErrorCode.RESOURCE_NOT_FOUND);

        // 409 CONFLICT Errors
        exceptionToErrorCodeMap.put(IllegalStateException.class, ErrorCode.ILLEGAL_STATE);

        // 500 INTERNAL_SERVER_ERROR Errors
        exceptionToErrorCodeMap.put(ArrayIndexOutOfBoundsException.class, ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS);
        exceptionToErrorCodeMap.put(CompletionException.class, ErrorCode.COMPLETION_ERROR);
        exceptionToErrorCodeMap.put(DataIntegrityViolationException.class, ErrorCode.DATA_INTEGRITY_ERROR);
        exceptionToErrorCodeMap.put(IOException.class, ErrorCode.IO_ERROR);
        exceptionToErrorCodeMap.put(IncorrectResultSizeDataAccessException.class, ErrorCode.INCORRECT_RESULT_SIZE);
        exceptionToErrorCodeMap.put(SQLException.class, ErrorCode.SQL_ERROR);
        exceptionToErrorCodeMap.put(SecurityException.class, ErrorCode.SECURITY_ERROR);
        exceptionToErrorCodeMap.put(TransactionSystemException.class, ErrorCode.TRANSACTION_SYSTEM_ERROR);
        exceptionToErrorCodeMap.put(UnexpectedRollbackException.class, ErrorCode.UNEXPECTED_ROLLBACK);
        exceptionToErrorCodeMap.put(MessagingException.class, ErrorCode.MESSAGING_ERROR);
        exceptionToErrorCodeMap.put(ImageProcessingException.class, ErrorCode.IMAGE_PROCESSING_ERROR);
        exceptionToErrorCodeMap.put(MetadataException.class, ErrorCode.METADATA_ERROR);
        exceptionToErrorCodeMap.put(WebClientRequestException.class, ErrorCode.WEB_CLIENT_REQUEST_ERROR);
    }

    /**
     * 예외에 맞는 ErrorCode를 반환합니다. 매핑되지 않은 예외는 기본적으로 INTERNAL_SERVER_ERROR로 반환됩니다.
     *
     * @param ex 예외
     * @return 매핑된 ErrorCode
     */
    public static ErrorCode getErrorCode(Exception ex) {
        return exceptionToErrorCodeMap.getOrDefault(ex.getClass(), ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
