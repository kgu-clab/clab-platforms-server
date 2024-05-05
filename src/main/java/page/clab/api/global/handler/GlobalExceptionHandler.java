package page.clab.api.global.handler;

import com.google.gson.stream.MalformedJsonException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import page.clab.api.domain.accuse.exception.AccuseTargetTypeIncorrectException;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotFinishedException;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotProgressingException;
import page.clab.api.domain.activityGroup.exception.AlreadyAppliedException;
import page.clab.api.domain.activityGroup.exception.DuplicateAbsentExcuseException;
import page.clab.api.domain.activityGroup.exception.DuplicateAttendanceException;
import page.clab.api.domain.activityGroup.exception.DuplicateReportException;
import page.clab.api.domain.activityGroup.exception.InvalidCategoryException;
import page.clab.api.domain.activityGroup.exception.InvalidParentBoardException;
import page.clab.api.domain.activityGroup.exception.LeaderStatusChangeNotAllowedException;
import page.clab.api.domain.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.book.exception.BookAlreadyAppliedForLoanException;
import page.clab.api.domain.book.exception.BookAlreadyBorrowedException;
import page.clab.api.domain.book.exception.BookAlreadyReturnedException;
import page.clab.api.domain.book.exception.InvalidBorrowerException;
import page.clab.api.domain.book.exception.LoanNotPendingException;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.book.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.book.exception.OverdueException;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.exception.AssociatedAccountExistsException;
import page.clab.api.domain.review.exception.AlreadyReviewedException;
import page.clab.api.domain.sharedAccount.exception.InvalidUsageTimeException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountUsageStateException;
import page.clab.api.global.auth.exception.AuthenticationInfoNotFoundException;
import page.clab.api.global.auth.exception.TokenForgeryException;
import page.clab.api.global.auth.exception.TokenMisuseException;
import page.clab.api.global.auth.exception.TokenNotFoundException;
import page.clab.api.global.auth.exception.TokenValidateException;
import page.clab.api.global.auth.exception.UnAuthorizeException;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.ErrorResponse;
import page.clab.api.global.common.file.exception.AssignmentFileUploadFailException;
import page.clab.api.global.common.file.exception.CloudStorageNotEnoughException;
import page.clab.api.global.common.file.exception.FileUploadFailException;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.DecryptionException;
import page.clab.api.global.exception.EncryptionException;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;

@RestControllerAdvice(basePackages = "page.clab.api")
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final SlackService slackService;

    @ExceptionHandler({
            InvalidInformationException.class,
            InvalidParentBoardException.class,
            InvalidCategoryException.class,
            StringIndexOutOfBoundsException.class,
            MissingServletRequestParameterException.class,
            MalformedJsonException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalAccessException.class,
    })
    public ErrorResponse badRequestException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler({
            AuthenticationInfoNotFoundException.class,
            UnAuthorizeException.class,
            AccessDeniedException.class,
            LoginFaliedException.class,
            MemberLockedException.class,
            BadCredentialsException.class,
            TokenValidateException.class,
            TokenNotFoundException.class,
            TokenMisuseException.class,
            TokenForgeryException.class,
            MessagingException.class,
    })
    public ApiResponse unAuthorizeException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
            PermissionDeniedException.class,
            InvalidBorrowerException.class,
    })
    public ApiResponse deniedException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
            NullPointerException.class,
            NotFoundException.class,
            NoSuchElementException.class,
            FileNotFoundException.class,
    })
    public ErrorResponse notFoundException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler({
            AssignmentFileUploadFailException.class
    })
    public ApiResponse notFoundException(HttpServletResponse response, AssignmentFileUploadFailException e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
            AccuseTargetTypeIncorrectException.class,
            NotApprovedApplicationException.class,
            AssociatedAccountExistsException.class,
            CloudStorageNotEnoughException.class,
            ActivityGroupNotFinishedException.class,
            ActivityGroupNotProgressingException.class,
            LeaderStatusChangeNotAllowedException.class,
            AlreadyAppliedException.class,
            DuplicateReportException.class,
            DuplicateAttendanceException.class,
            DuplicateAbsentExcuseException.class,
            AlreadyReviewedException.class,
            BookAlreadyBorrowedException.class,
            BookAlreadyReturnedException.class,
            BookAlreadyAppliedForLoanException.class,
            MaxBorrowLimitExceededException.class,
            OverdueException.class,
            LoanSuspensionException.class,
            LoanNotPendingException.class,
            SharedAccountUsageStateException.class,
            InvalidUsageTimeException.class,
    })
    public ErrorResponse conflictException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler({
            IllegalStateException.class,
            FileUploadFailException.class,
            DataIntegrityViolationException.class,
            IncorrectResultSizeDataAccessException.class,
            ArrayIndexOutOfBoundsException.class,
            IOException.class,
            WebClientRequestException.class,
            TransactionSystemException.class,
            SecurityException.class,
            CustomOptimisticLockingFailureException.class,
            CompletionException.class,
            EncryptionException.class,
            DecryptionException.class,
            Exception.class
    })
    public ApiResponse serverException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        slackService.sendServerErrorNotification(request, e);
        log.warn(e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ApiResponse handleValidationException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ApiResponse.failure();
    }

}