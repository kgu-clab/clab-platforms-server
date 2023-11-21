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
import javax.servlet.http.HttpServletRequest;
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
import page.clab.api.exception.ActivityGroupNotFinishedException;
import page.clab.api.exception.AlreadyReviewedException;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.BookAlreadyBorrowedException;
import page.clab.api.exception.DuplicateLoginException;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.exception.InvalidBorrowerException;
import page.clab.api.exception.LoanSuspensionException;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.OverdueException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.type.dto.ResponseModel;

@RestControllerAdvice(basePackages = "page.clab.api.controller")
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({
            NullPointerException.class,
            SearchResultNotExistException.class,
            NotFoundException.class
    })
    public ResponseModel Exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(true)
                .build();
        response.setStatus(200);
        return responseModel;
    }

    @ExceptionHandler({
            NoSuchElementException.class,
            MissingServletRequestParameterException.class,
            MalformedJsonException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            DataIntegrityViolationException.class,
            FileUploadFailException.class,
            UnAuthorizeException.class,
            AccessDeniedException.class,
            PermissionDeniedException.class,
            TokenValidateException.class,
            LoginFaliedException.class,
            MemberLockedException.class,
            BadCredentialsException.class,
            FileNotFoundException.class,
            AssociatedAccountExistsException.class,
            GeoIp2Exception.class,
            AddressNotFoundException.class,
            IOException.class,
            WebClientRequestException.class,
            BookAlreadyBorrowedException.class,
            InvalidBorrowerException.class,
            LoanSuspensionException.class,
            OverdueException.class,
            TransactionSystemException.class,
            StringIndexOutOfBoundsException.class,
            MessagingException.class,
            DuplicateLoginException.class,
            ActivityGroupNotFinishedException.class,
            AlreadyReviewedException.class,
            Exception.class
    })
    public ResponseModel errorException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .build();
        response.setStatus(200);
        return responseModel;
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseModel handleValidationException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<Map<String, String>> errorList = new ArrayList<>();

        for (FieldError fieldError : result.getFieldErrors()) {
            Map<String, String> error = new HashMap<>();
            error.put("fieldName", fieldError.getField());
            error.put("message", getMessage(fieldError.getDefaultMessage()));
            errorList.add(error);
        }

        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .data(errorList)
                .build();
        response.setStatus(200);
        log.info("Validation error: {}", errorList);
        return responseModel;
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

}