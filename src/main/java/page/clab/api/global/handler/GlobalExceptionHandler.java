package page.clab.api.global.handler;

import com.google.gson.stream.MalformedJsonException;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
import page.clab.api.domain.accuse.exception.AccuseSearchArgumentLackException;
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
import page.clab.api.domain.activityGroup.exception.NotSubmitCategoryBoardException;
import page.clab.api.domain.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.book.exception.BookAlreadyBorrowedException;
import page.clab.api.domain.book.exception.BookAlreadyReturnedException;
import page.clab.api.domain.book.exception.InvalidBorrowerException;
import page.clab.api.domain.book.exception.LoanNotPendingException;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.book.exception.MaxBorrowLimitExceededException;
import page.clab.api.domain.book.exception.OverdueException;
import page.clab.api.domain.donation.exception.DonationSearchArgumentLackException;
import page.clab.api.domain.login.exception.DuplicateLoginException;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.exception.AssociatedAccountExistsException;
import page.clab.api.domain.review.exception.AlreadyReviewedException;
import page.clab.api.domain.sharedAccount.exception.InvalidUsageTimeException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountInUseException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountUsageStateException;
import page.clab.api.global.auth.exception.AuthenticationInfoNotFoundException;
import page.clab.api.global.auth.exception.TokenForgeryException;
import page.clab.api.global.auth.exception.TokenMisuseException;
import page.clab.api.global.auth.exception.TokenNotFoundException;
import page.clab.api.global.auth.exception.TokenValidateException;
import page.clab.api.global.auth.exception.UnAuthorizeException;
import page.clab.api.global.common.dto.ResponseModel;
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
import page.clab.api.global.exception.SearchResultNotExistException;
import page.clab.api.global.exception.SecretKeyCreationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "page.clab.api")
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final SlackService slackService;

    @ExceptionHandler({
            ActivityGroupNotFinishedException.class,
            InvalidInformationException.class,
            InvalidParentBoardException.class,
            InvalidCategoryException.class,
            OverdueException.class,
            StringIndexOutOfBoundsException.class,
            MissingServletRequestParameterException.class,
            MalformedJsonException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalAccessException.class,
            ActivityGroupNotProgressingException.class,
            LeaderStatusChangeNotAllowedException.class,
            CloudStorageNotEnoughException.class,
            NotSubmitCategoryBoardException.class,
            AccuseTargetTypeIncorrectException.class,
            AccuseSearchArgumentLackException.class,
            NotApprovedApplicationException.class,
            DonationSearchArgumentLackException.class
    })
    public ResponseModel badRequestException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ResponseModel.failure();
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
    public ResponseModel unAuthorizeException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ResponseModel.failure();
    }

    @ExceptionHandler({
            PermissionDeniedException.class,
            LoanSuspensionException.class,
            InvalidBorrowerException.class,
    })
    public ResponseModel deniedException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return ResponseModel.failure();
    }

    @ExceptionHandler({
            NullPointerException.class,
            SearchResultNotExistException.class,
            NotFoundException.class,
            NoSuchElementException.class,
            FileNotFoundException.class,
            AddressNotFoundException.class,
    })
    public ResponseModel<List<?>> notFoundException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseModel.success(new ArrayList<>());
    }

    @ExceptionHandler({
            AssignmentFileUploadFailException.class
    })
    public ResponseModel notFoundException(HttpServletResponse response, AssignmentFileUploadFailException e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseModel.failure();
    }

    @ExceptionHandler({
            AssociatedAccountExistsException.class,
            BookAlreadyBorrowedException.class,
            BookAlreadyReturnedException.class,
            MaxBorrowLimitExceededException.class,
            LoanNotPendingException.class,
            DuplicateLoginException.class,
            AlreadyReviewedException.class,
            AlreadyAppliedException.class,
            DuplicateAttendanceException.class,
            DuplicateAbsentExcuseException.class,
            DuplicateReportException.class,
            InvalidUsageTimeException.class,
            SharedAccountInUseException.class,
            SharedAccountUsageStateException.class
    })
    public ResponseModel conflictException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return ResponseModel.failure();
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
            SecretKeyCreationException.class,
            EncryptionException.class,
            DecryptionException.class,
            Exception.class
    })
    public ResponseModel serverException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        slackService.sendServerErrorNotification(request, e);
        log.warn(e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return ResponseModel.failure();
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ResponseModel handleValidationException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ResponseModel.failure();
    }

}