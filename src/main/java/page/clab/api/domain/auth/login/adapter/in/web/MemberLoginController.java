package page.clab.api.domain.auth.login.adapter.in.web;

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
import page.clab.api.domain.auth.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.auth.login.application.dto.response.LoginResult;
import page.clab.api.domain.auth.login.application.port.in.ManageLoginUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "Authentication - Login", description = "로그인")
public class MemberLoginController {

    private final ManageLoginUseCase manageLoginUseCase;

    private final String authHeader;

    public MemberLoginController(
        @Qualifier("userLoginService") ManageLoginUseCase manageLoginUseCase,
        @Value("${security.auth.header}") String authHeader
    ) {
        this.manageLoginUseCase = manageLoginUseCase;
        this.authHeader = authHeader;
    }

    @Operation(summary = "멤버 로그인", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("")
    public ApiResponse<Boolean> login(
        HttpServletRequest request,
        HttpServletResponse response,
        @Valid @RequestBody LoginRequestDto requestDto
    ) {
        LoginResult result = manageLoginUseCase.login(request, requestDto);
        response.setHeader(authHeader, result.getHeader());
        return ApiResponse.success(result.getBody());
    }

    @Operation(summary = "Guest 로그인", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("/guest")
    public ApiResponse<Boolean> guestLogin(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        LoginResult result = manageLoginUseCase.guestLogin(request);
        response.setHeader(authHeader, result.getHeader());
        return ApiResponse.success(result.getBody());
    }
}
