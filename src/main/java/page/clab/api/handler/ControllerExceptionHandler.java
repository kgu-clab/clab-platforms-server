package page.clab.api.handler;

import com.google.gson.stream.MalformedJsonException;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import page.clab.api.auth.exception.TokenValidateException;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.exception.AccountInUseException;
import page.clab.api.exception.ActivityGroupNotFinishedException;
import page.clab.api.exception.AlreadyReviewedException;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.BookAlreadyBorrowedException;
import page.clab.api.exception.CustomOptimisticLockingFailureException;
import page.clab.api.exception.DuplicateAbsentExcuseException;
import page.clab.api.exception.DuplicateAttendanceException;
import page.clab.api.exception.DuplicateLoginException;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.exception.InvalidBorrowerException;
import page.clab.api.exception.InvalidInformationException;
import page.clab.api.exception.LoanSuspensionException;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.OverdueException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.exception.SharedAccountUsageStateException;
import page.clab.api.type.dto.ResponseModel;

@RestControllerAdvice(basePackages = "page.clab.api.controller")
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({
            ActivityGroupNotFinishedException.class,
            InvalidInformationException.class,
            OverdueException.class,
            StringIndexOutOfBoundsException.class,
            MissingServletRequestParameterException.class,
            MalformedJsonException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalAccessException.class
    })
    public ResponseModel badRequestException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return makeExceptionResponseModel(null);
    }

    @ExceptionHandler({
            UnAuthorizeException.class,
            AccessDeniedException.class,
            LoginFaliedException.class,
            MemberLockedException.class,
            BadCredentialsException.class,
            TokenValidateException.class,
            MessagingException.class,
    })
    public ResponseModel unAuthorizeException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return makeExceptionResponseModel(null);
    }

    @ExceptionHandler({
            PermissionDeniedException.class,
            LoanSuspensionException.class,
            InvalidBorrowerException.class,
    })
    public ResponseModel deniedException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return makeExceptionResponseModel(null);
    }

    @ExceptionHandler({
            NullPointerException.class,
            SearchResultNotExistException.class,
            NotFoundException.class,
            NoSuchElementException.class,
            FileNotFoundException.class,
            AddressNotFoundException.class,
    })
    public ResponseModel notFoundException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return makeExceptionResponseModel(null);
    }

    @ExceptionHandler({
            AssociatedAccountExistsException.class,
            BookAlreadyBorrowedException.class,
            DuplicateLoginException.class,
            AlreadyReviewedException.class,
            DuplicateAttendanceException.class,
            DuplicateAbsentExcuseException.class
            AccountInUseException.class,
            SharedAccountUsageStateException.class
    })
    public ResponseModel conflictException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return makeExceptionResponseModel(null);
    }

    @ExceptionHandler({
            IllegalStateException.class,
            FileUploadFailException.class,
            DataIntegrityViolationException.class,
            GeoIp2Exception.class,
            IOException.class,
            WebClientRequestException.class,
            TransactionSystemException.class,
            SecurityException.class,
            CustomOptimisticLockingFailureException.class,
            Exception.class
    })
    public ResponseModel serverException(HttpServletResponse response, Exception e){
        log.warn(e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return makeExceptionResponseModel(null);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseModel handleValidationException(HttpServletResponse response, MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<Map<String, String>> errorList = new ArrayList<>();

        for (FieldError fieldError : result.getFieldErrors()) {
            Map<String, String> error = new HashMap<>();
            error.put("fieldName", fieldError.getField());
            error.put("message", getMessage(fieldError.getDefaultMessage()));
            errorList.add(error);
        }

        log.info("Validation error: {}", errorList);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return makeExceptionResponseModel(null);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    private ResponseModel makeExceptionResponseModel(String message) {
        return ResponseModel.builder()
                .success(false)
                .data(message)
                .build();
    }

}