package page.clab.api.domain.blacklistIp.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.blacklistIp.application.port.in.RemoveBlacklistIpUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blacklists")
@RequiredArgsConstructor
@Tag(name = "Blacklist IP", description = "블랙리스트 IP")
public class BlacklistIpRemoveController {

    private final RemoveBlacklistIpUseCase removeBlacklistIpUseCase;

    @Operation(summary = "[S] 블랙리스트 IP 제거", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({ "ROLE_SUPER" })
    @DeleteMapping("")
    public ApiResponse<String> removeBlacklistIp(
            HttpServletRequest request,
            @RequestParam(name = "ipAddress") String ipAddress
    ) {
        String deletedIp = removeBlacklistIpUseCase.removeBlacklistIp(request, ipAddress);
        return ApiResponse.success(deletedIp);
    }
}
