package page.clab.api.domain.auth.login.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.auth.login.application.dto.response.LoginResult;
import page.clab.api.domain.auth.login.application.exception.LoginFailedException;
import page.clab.api.domain.auth.login.application.exception.MemberLockedException;
import page.clab.api.domain.auth.login.application.port.in.ManageLoginUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/login/authenticator")
@Tag(name = "Authentication - Two Factor Authentication", description = "2단계 인증")
public class TwoFactorAuthenticationController {

    private final ManageLoginUseCase manageLoginUseCase;

    private final String authHeader;

    public TwoFactorAuthenticationController(
        @Qualifier("twoFactorAuthenticationService") ManageLoginUseCase manageLoginUseCase,
        @Value("${security.auth.header}") String authHeader
    ) {
        this.manageLoginUseCase = manageLoginUseCase;
        this.authHeader = authHeader;
    }

    @Operation(summary = "TOTP 인증", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("")
    public ApiResponse<Boolean> authenticate(
        HttpServletRequest request,
        HttpServletResponse response,
        @Valid @RequestBody TwoFactorAuthenticationRequestDto requestDto
    ) throws MemberLockedException, LoginFailedException {
        LoginResult result = manageLoginUseCase.authenticate(request, requestDto);
        response.setHeader(authHeader, result.getHeader());
        return ApiResponse.success(result.getBody());
    }

    @Operation(summary = "[S] TOTP 초기화", description = "ROLE_SUPER 권한이 필요함")
    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponse<String> resetAuthenticator(
        @PathVariable(name = "memberId") String memberId
    ) {
        String id = manageLoginUseCase.resetAuthenticator(memberId);
        return ApiResponse.success(id);
    }
}
