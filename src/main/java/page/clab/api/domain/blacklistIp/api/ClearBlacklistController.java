package page.clab.api.domain.blacklistIp.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.blacklistIp.application.ClearBlacklistService;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blacklists")
@RequiredArgsConstructor
@Tag(name = "Blacklist IP", description = "블랙리스트 IP")
public class ClearBlacklistController {

    private final ClearBlacklistService clearBlacklistService;

    @Operation(summary = "[S] 블랙리스트 IP 초기화", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/clear")
    public ApiResponse<List<String>> clearBlacklist(
            HttpServletRequest request
    ) {
        List<String> blacklistIps = clearBlacklistService.execute(request);
        return ApiResponse.success(blacklistIps);
    }
}