package page.clab.api.domain.login.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.port.in.LoginUseCase;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.exception.LoginFailedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "Login", description = "로그인")
public class MemberLoginController {

    private final LoginUseCase loginUseCase;

    private final String authHeader;

    public MemberLoginController(
            @Qualifier("userLoginService") LoginUseCase loginUseCase,
            @Value("${security.auth.header}") String authHeader
    ) {
        this.loginUseCase = loginUseCase;
        this.authHeader = authHeader;
    }

    @Operation(summary = "멤버 로그인", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("")
    public ApiResponse<Boolean> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody LoginRequestDto requestDto
    ) throws MemberLockedException, LoginFailedException {
        LoginResult result = loginUseCase.login(request, requestDto);
        response.setHeader(authHeader, result.getHeader());
        return ApiResponse.success(result.getBody());
    }
}
