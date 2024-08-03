package page.clab.api.domain.auth.redisIpAccessMonitor.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.ClearAbnormalAccessIpsUseCase;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ip-access-monitor")
@RequiredArgsConstructor
@Tag(name = "Authentication - Abnormal Access IP", description = "비정상 접근 IP")
public class AbnormalAccessIpsClearController {

    private final ClearAbnormalAccessIpsUseCase clearAbnormalAccessIpsUseCase;

    @Operation(summary = "[S] 비정상 접근 IP 기록 초기화", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "지속적인 비정상 접근으로 인해 차단된 IP를 모두 삭제")
    @Secured({ "ROLE_SUPER" })
    @DeleteMapping("/abnormal-access/clear")
    public ApiResponse<List<RedisIpAccessMonitor>> clearAbnormalAccessBlacklist(
            HttpServletRequest request
    ) {
        List<RedisIpAccessMonitor> ipAccessMonitors = clearAbnormalAccessIpsUseCase.clearAbnormalAccessIps(request);
        return ApiResponse.success(ipAccessMonitors);
    }
}
