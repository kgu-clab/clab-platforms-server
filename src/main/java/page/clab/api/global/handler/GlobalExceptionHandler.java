package page.clab.api.global.handler;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.google.gson.stream.MalformedJsonException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.UnknownPathException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import page.clab.api.domain.memberManagement.executive.application.exception.ExecutiveRegistrationException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberContactException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberEmailException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberIdException;
import page.clab.api.domain.memberManagement.member.application.exception.InvalidRoleChangeException;
import page.clab.api.global.auth.exception.AuthenticationInfoNotFoundException;
import page.clab.api.global.auth.exception.TokenForgeryException;
import page.clab.api.global.auth.exception.TokenMisuseException;
import page.clab.api.global.auth.exception.TokenNotFoundException;
import page.clab.api.global.auth.exception.TokenValidateException;
import page.clab.api.global.auth.exception.UnAuthorizeException;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.ErrorResponse;
import page.clab.api.global.common.file.exception.CloudStorageNotEnoughException;
import page.clab.api.global.common.file.exception.DirectoryCreationException;
import page.clab.api.global.common.file.exception.FilePermissionException;
import page.clab.api.global.common.file.exception.FileUploadFailException;
import page.clab.api.global.common.file.exception.InvalidFileAttributeException;
import page.clab.api.global.common.file.exception.InvalidPathVariableException;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.GeneralAlertType;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.InvalidDateRangeException;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;

@RestControllerAdvice(basePackages = "page.clab.api")
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler({
        InvalidInformationException.class,
//        InvalidParentBoardException.class,
//        InvalidCategoryException.class,
        InvalidDateRangeException.class,
        InvalidColumnException.class,
        InvalidEmojiException.class,
        InvalidRoleChangeException.class,
//        InvalidRoleException.class,
        InvalidFileAttributeException.class,
//        InvalidGithubUrlException.class,
//        InactiveMemberException.class,
//        DuplicateRoleException.class,
//        RecruitmentNotActiveException.class,
//        RecruitmentEndDateExceededException.class,
        StringIndexOutOfBoundsException.class,
        MissingServletRequestParameterException.class,
        MalformedJsonException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        IllegalAccessException.class,
        NumberFormatException.class,
        SortingArgumentException.class,
        UnknownPathException.class,
//        AssignmentBoardHasNoDueDateTimeException.class,
//        FeedbackBoardHasNoContentException.class,
//        InvalidBoardCategoryHashtagException.class,
        ExecutiveRegistrationException.class
    })
    public ErrorResponse<Exception> badRequestException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler({
        AuthenticationException.class,
        AuthenticationInfoNotFoundException.class,
        UnAuthorizeException.class,
//        LoginFailedException.class,
//        MemberLockedException.class,
        BadCredentialsException.class,
        TokenValidateException.class,
        TokenNotFoundException.class,
        TokenMisuseException.class,
        TokenForgeryException.class,
        MessagingException.class,
    })
    public ApiResponse<Void> unAuthorizeException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
        AccessDeniedException.class,
        PermissionDeniedException.class,
//        InvalidBorrowerException.class,
//        MemberNotPartOfActivityException.class,
    })
    public ApiResponse<Void> deniedException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
        NullPointerException.class,
        NotFoundException.class,
        NoSuchElementException.class,
        FileNotFoundException.class,
    })
    public ErrorResponse<Exception> notFoundException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler({
        InvalidPathVariableException.class
    })
    public ApiResponse<Void> notFoundException(HttpServletResponse response, InvalidPathVariableException e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
//        AccuseTargetTypeIncorrectException.class,
//        NotApprovedApplicationException.class,
        DuplicateMemberIdException.class,
        DuplicateMemberContactException.class,
        DuplicateMemberEmailException.class,
        CloudStorageNotEnoughException.class,
//        ActivityGroupNotFinishedException.class,
//        ActivityGroupNotProgressingException.class,
//        AlreadySubmittedThisWeekAssignmentException.class,
//        LeaderStatusChangeNotAllowedException.class,
//        SingleLeaderModificationException.class,
//        AlreadyAppliedException.class,
//        DuplicateReportException.class,
//        DuplicateAttendanceException.class,
//        DuplicateAbsentExcuseException.class,
//        AlreadyReviewedException.class,
//        BookAlreadyBorrowedException.class,
//        BookAlreadyReturnedException.class,
//        BookAlreadyAppliedForLoanException.class,
//        MaxBorrowLimitExceededException.class,
//        OverdueException.class,
//        LoanSuspensionException.class,
//        LoanNotPendingException.class,
    })
    public ErrorResponse<Exception> conflictException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        return ErrorResponse.failure(e);
    }

    @ExceptionHandler({
        IllegalStateException.class,
        FileUploadFailException.class,
        FilePermissionException.class,
        DirectoryCreationException.class,
        DataIntegrityViolationException.class,
        IncorrectResultSizeDataAccessException.class,
        ArrayIndexOutOfBoundsException.class,
        IOException.class,
        WebClientRequestException.class,
        TransactionSystemException.class,
        SecurityException.class,
//        CustomOptimisticLockingFailureException.class,
        CompletionException.class,
//        EncryptionException.class,
//        DecryptionException.class,
        InvalidDataAccessApiUsageException.class,
        ImageProcessingException.class,
        MetadataException.class,
        Exception.class
    })
    public ApiResponse<Void> serverException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        eventPublisher.publishEvent(
            new NotificationEvent(this, GeneralAlertType.SERVER_ERROR, request, e));
        log.warn(e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return ApiResponse.failure();
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class
    })
    public ApiResponse<Void> handleValidationException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ApiResponse.failure();
    }
}
