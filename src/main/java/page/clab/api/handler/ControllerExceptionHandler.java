package page.clab.api.handler;

import com.google.gson.stream.MalformedJsonException;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import page.clab.api.auth.exception.TokenValidateException;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.FileUploadFailException;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.type.dto.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "page.clab.api.controller")
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({
            NoSuchElementException.class,
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
            Exception.class
    })
    public ResponseModel errorException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .build();
        response.setStatus(200);
        return responseModel;
    }

//    @ExceptionHandler({
//            NoSuchElementException.class,
//            MissingServletRequestParameterException.class,
//            MalformedJsonException.class,
//            HttpMessageNotReadableException.class,
//            MethodArgumentTypeMismatchException.class,
//            NullPointerException.class,
//            DataIntegrityViolationException.class,
//            FileUploadFailException.class
//    })
//    public ResponseModel parameterError(HttpServletRequest request, HttpServletResponse response, Exception e) {
//        ResponseModel responseModel = ResponseModel.builder()
//                .success(false)
//                .build();
//        response.setStatus(400);
//        return responseModel;
//    }
//
//    @ExceptionHandler({
//            UnAuthorizeException.class,
//            AccessDeniedException.class,
//            PermissionDeniedException.class,
//            TokenValidateException.class
//    })
//    public ResponseModel unAuthorizeRequestError(HttpServletRequest request, HttpServletResponse response,
//                                                 Exception e) {
//        ResponseModel responseModel = ResponseModel.builder()
//                .success(false)
//                .build();
//        response.setStatus(401);
//        return responseModel;
//    }
//
//    @ExceptionHandler({
//            LoginFaliedException.class,
//            MemberLockedException.class,
//            BadCredentialsException.class
//    })
//    public ResponseModel LoginFailedError(HttpServletRequest request, HttpServletResponse response,
//                                          Exception e) throws Exception {
//        ResponseModel responseModel = ResponseModel.builder()
//                .success(false)
//                .build();
//        response.setStatus(403);
//        return responseModel;
//    }
//
//    @ExceptionHandler({
//            SearchResultNotExistException.class,
//            FileNotFoundException.class,
//            NotFoundException.class
//    })
//    public ResponseModel searchResultNotExistError(HttpServletRequest request, HttpServletResponse response,
//                                                   Exception e) {
//        ResponseModel responseModel = ResponseModel.builder()
//                .success(false)
//                .build();
//        response.setStatus(404);
//        return responseModel;
//    }
//
//    @ExceptionHandler({
//            AssociatedAccountExistsException.class
//    })
//    public ResponseModel AssociatedAccountExistsExceptionError(HttpServletRequest request, HttpServletResponse response,
//                                                               Exception e) {
//        ResponseModel responseModel = ResponseModel.builder()
//                .success(false)
//                .build();
//        response.setStatus(404);
//        return responseModel;
//    }
//
//    @ExceptionHandler({
//            Exception.class
//    })
//    public ResponseModel unExceptedError(HttpServletRequest request, HttpServletResponse response, Exception e) {
//        ResponseModel responseModel = ResponseModel.builder()
//                .success(false)
//                .build();
//        response.setStatus(500);
//        return responseModel;
//    }

}