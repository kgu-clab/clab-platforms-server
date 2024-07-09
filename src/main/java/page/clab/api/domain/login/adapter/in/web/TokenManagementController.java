package page.clab.api.domain.login.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.dto.response.TokenHeader;
import page.clab.api.domain.login.application.port.in.ManageLoginUseCase;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "Login", description = "로그인")
@Slf4j
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

    @Operation(summary = "[S] 멤버 토큰 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @DeleteMapping("/revoke/{memberId}")
    @Secured({ "ROLE_SUPER" })
    public ApiResponse<String> revokeToken(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = manageLoginUseCase.revokeToken(memberId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 멤버 토큰 재발급", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/reissue")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
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
    @Secured({ "ROLE_SUPER" })
    public ApiResponse<List<String>> retrieveCurrentLoggedInUsers() {
        List<String> currentLoggedInUsers = manageLoginUseCase.retrieveCurrentLoggedInUsers();
        return ApiResponse.success(currentLoggedInUsers);
    }
}
