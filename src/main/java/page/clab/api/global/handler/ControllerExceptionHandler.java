package page.clab.api.global.handler;

import com.google.gson.stream.MalformedJsonException;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotFinishedException;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotProgressingException;
import page.clab.api.domain.activityGroup.exception.DuplicateAbsentExcuseException;
import page.clab.api.domain.activityGroup.exception.DuplicateAttendanceException;
import page.clab.api.domain.activityGroup.exception.DuplicateReportException;
import page.clab.api.domain.activityGroup.exception.NotAProjectGroupException;
import page.clab.api.domain.activityGroup.exception.NotAStudyGroupException;
import page.clab.api.domain.book.exception.BookAlreadyBorrowedException;
import page.clab.api.domain.book.exception.InvalidBorrowerException;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.book.exception.OverdueException;
import page.clab.api.domain.login.exception.DuplicateLoginException;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.exception.AssociatedAccountExistsException;
import page.clab.api.domain.review.exception.AlreadyReviewedException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountInUseException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountUsageStateException;
import page.clab.api.global.auth.exception.TokenValidateException;
import page.clab.api.global.auth.exception.UnAuthorizeException;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.common.file.exception.CloudStorageNotEnoughException;
import page.clab.api.global.common.file.exception.FileUploadFailException;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SearchResultNotExistException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "page.clab.api")
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionHandler {

    private final SlackService slackService;

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
            IllegalAccessException.class,
            NotAStudyGroupException.class,
            NotAProjectGroupException.class,
            ActivityGroupNotProgressingException.class,
            CloudStorageNotEnoughException.class
    })
    public ResponseModel badRequestException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return makeExceptionResponseModel(false, null);
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
        return makeExceptionResponseModel(false, null);
    }

    @ExceptionHandler({
            PermissionDeniedException.class,
            LoanSuspensionException.class,
            InvalidBorrowerException.class,
    })
    public ResponseModel deniedException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return makeExceptionResponseModel(false, null);
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
        response.setStatus(HttpServletResponse.SC_OK);
        return makeExceptionResponseModel(true, new ArrayList<>());
    }

    @ExceptionHandler({
            AssociatedAccountExistsException.class,
            BookAlreadyBorrowedException.class,
            DuplicateLoginException.class,
            AlreadyReviewedException.class,
            DuplicateAttendanceException.class,
            DuplicateAbsentExcuseException.class,
            DuplicateReportException.class,
            SharedAccountInUseException.class,
            SharedAccountUsageStateException.class
    })
    public ResponseModel conflictException(HttpServletResponse response, Exception e){
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return makeExceptionResponseModel(false, null);
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
    public ResponseModel serverException(HttpServletRequest request, HttpServletResponse response, Exception e){
        slackService.sendServerErrorNotification(request, e);
        log.warn(e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return makeExceptionResponseModel(false, null);
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
        return makeExceptionResponseModel(false, null);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    private ResponseModel makeExceptionResponseModel(boolean success, Object message) {
        return ResponseModel.builder()
                .success(success)
                .data(message)
                .build();
    }

}