package page.clab.api.domain.login.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.domain.login.application.port.in.RetrieveLoginAttemptLogsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/login-attempt-logs")
@RequiredArgsConstructor
@Tag(name = "LoginAttemptLog", description = "로그인 시도 로그")
@Slf4j
public class LoginAttemptLogsRetrievalController {

    private final RetrieveLoginAttemptLogsUseCase retrieveLoginAttemptLogsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[S] 계정별 로그인 시도 로그 조회", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @Secured({ "ROLE_SUPER" })
    @GetMapping("/{memberId}")
    public ApiResponse<PagedResponseDto<LoginAttemptLogResponseDto>> retrieveLoginAttemptLogs(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "loginAttemptTime") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, LoginAttemptLogResponseDto.class);
        PagedResponseDto<LoginAttemptLogResponseDto> loginAttemptLogs = retrieveLoginAttemptLogsUseCase.retrieveLoginAttemptLogs(memberId, pageable);
        return ApiResponse.success(loginAttemptLogs);
    }
}
