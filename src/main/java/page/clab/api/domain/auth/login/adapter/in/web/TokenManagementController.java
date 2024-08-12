package page.clab.api.domain.auth.login.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.login.application.dto.response.TokenHeader;
import page.clab.api.domain.auth.login.application.port.in.ManageLoginUseCase;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "Authentication - Token Management", description = "토큰 관리")
public class TokenManagementController {

    private final ManageLoginUseCase manageLoginUseCase;

    private final String authHeader;

    public TokenManagementController(
            @Qualifier("tokenManagementService") ManageLoginUseCase manageLoginUseCase,
            @Value("${security.auth.header}") String authHeader
    ) {
        this.manageLoginUseCase = manageLoginUseCase;
        this.authHeader = authHeader;
    }

    @Operation(summary = "[G] 토큰 재발급", description = "ROLE_GUEST 이상의 권한이 필요함")
    @PostMapping("/reissue")
    @PreAuthorize("hasRole('GUEST')")
    public ApiResponse<Void> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        TokenHeader headerData = manageLoginUseCase.reissueToken(request);
        response.setHeader(authHeader, headerData.toJson());
        return ApiResponse.success();
    }

    @Operation(summary = "[S] 현재 로그인 중인 멤버 조회", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "Redis에 저장된 토큰을 조회하여 현재 로그인 중인 멤버를 조회합니다.")
    @GetMapping("/current")
    @PreAuthorize("hasRole('SUPER')")
    public ApiResponse<List<String>> retrieveCurrentLoggedInUsers() {
        List<String> currentLoggedInUsers = manageLoginUseCase.retrieveCurrentLoggedInUsers();
        return ApiResponse.success(currentLoggedInUsers);
    }
}
