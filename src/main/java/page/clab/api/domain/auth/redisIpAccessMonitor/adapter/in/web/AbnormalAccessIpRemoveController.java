package page.clab.api.domain.auth.redisIpAccessMonitor.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.RemoveAbnormalAccessIpUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/ip-access-monitor")
@RequiredArgsConstructor
@Tag(name = "Authentication - Abnormal Access IP", description = "비정상 접근 IP")
public class AbnormalAccessIpRemoveController {

    private final RemoveAbnormalAccessIpUseCase removeAbnormalAccessIpUseCase;

    @Operation(summary = "[S] 비정상 접근 IP 기록 삭제", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
        "지속적인 비정상 접근으로 인해 차단된 IP를 삭제")
    @PreAuthorize("hasRole('SUPER')")
    @DeleteMapping("/abnormal-access")
    public ApiResponse<String> removeAbnormalAccessBlacklistIp(
        HttpServletRequest request,
        @RequestParam(name = "ipAddress") String ipAddress
    ) {
        String deletedIp = removeAbnormalAccessIpUseCase.removeAbnormalAccessIp(request, ipAddress);
        return ApiResponse.success(deletedIp);
    }
}
