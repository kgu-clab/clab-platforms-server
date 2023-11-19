package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.LoginAttemptLogService;
import page.clab.api.type.dto.LoginAttemptLogResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/login-attempt-logs")
@RequiredArgsConstructor
@Tag(name = "LoginAttemptLog", description = "로그인 시도 로그 관련 API")
@Slf4j
public class LoginAttemptLogController {

    private final LoginAttemptLogService loginAttemptLogService;

    @Operation(summary = "[A] 계정별 로그인 시도 로그 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("/{memberId}")
    public ResponseModel getLoginAttemptLogs(
            @PathVariable String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        List<LoginAttemptLogResponseDto> loginAttemptLogs = loginAttemptLogService.getLoginAttemptLogs(memberId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(loginAttemptLogs);
        return responseModel;
    }

}
