package page.clab.api.domain.auth.blacklistIp.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.blacklistIp.application.port.in.ResetBlacklistIpsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blacklists")
@RequiredArgsConstructor
@Tag(name = "Authentication - Blacklist IP", description = "블랙리스트 IP")
public class BlacklistResetController {

    private final ResetBlacklistIpsUseCase resetBlacklistIpsUseCase;

    @Operation(summary = "[S] 블랙리스트 IP 초기화", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @DeleteMapping("/clear")
    public ApiResponse<List<String>> resetBlacklistIps(
            HttpServletRequest request
    ) {
        List<String> blacklistIps = resetBlacklistIpsUseCase.resetBlacklistIps(request);
        return ApiResponse.success(blacklistIps);
    }
}
