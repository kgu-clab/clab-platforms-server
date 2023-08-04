package page.clab.api.handler;

import com.google.gson.stream.MalformedJsonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import page.clab.api.auth.exception.TokenValidateException;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.type.dto.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "page.clab.api.controller")
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({
            NoSuchElementException.class,
            MissingServletRequestParameterException.class,
            MalformedJsonException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            NullPointerException.class,
            DataIntegrityViolationException.class
    })
    public ResponseModel parameterError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .build();
        response.setStatus(400);
        return responseModel;
    }

    @ExceptionHandler({
            UnAuthorizeException.class,
            AccessDeniedException.class,
            PermissionDeniedException.class,
            TokenValidateException.class
    })
    public ResponseModel unAuthorizeRequestError(HttpServletRequest request, HttpServletResponse response,
                                                 Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .build();
        response.setStatus(401);
        return responseModel;
    }

    @ExceptionHandler({
            SearchResultNotExistException.class
    })
    public ResponseModel searchResultNotExistError(HttpServletRequest request, HttpServletResponse response,
                                                   Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .build();
        response.setStatus(404);
        return responseModel;
    }

    @ExceptionHandler({
            NotFoundException.class,
            Exception.class
    })
    public ResponseModel unExceptedError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ResponseModel responseModel = ResponseModel.builder()
                .success(false)
                .build();
        response.setStatus(500);
        return responseModel;
    }

}