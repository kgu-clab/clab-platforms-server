package page.clab.api.global.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.global.auth.application.RedisIpAccessMonitorService;
import page.clab.api.global.auth.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ip-access-monitor")
@RequiredArgsConstructor
@Tag(name = "IP Access Monitor", description = "IP 접근 모니터링")
@Slf4j
public class RedisIpAccessMonitorController {

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;

    @Operation(summary = "[S] 비정상 접근으로 인한 차단 IP 목록 조회", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "지속적인 비정상 접근으로 인해 Redis에 추가된 IP를 조회")
    @Secured({ "ROLE_SUPER" })
    @GetMapping("/abnormal-access")
    public ApiResponse<PagedResponseDto<RedisIpAccessMonitor>> getAbnormalAccessBlacklistIps(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<RedisIpAccessMonitor> abnormalAccessBlacklistIps = redisIpAccessMonitorService.getAbnormalAccessIps(pageable);
        return ApiResponse.success(abnormalAccessBlacklistIps);
    }

    @Operation(summary = "[S] 비정상 접근 IP 기록 삭제", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "지속적인 비정상 접근으로 인해 차단된 IP를 삭제")
    @Secured({ "ROLE_SUPER" })
    @DeleteMapping("/abnormal-access")
    public ApiResponse<String> removeAbnormalAccessBlacklistIp(
            HttpServletRequest request,
            @RequestParam(name = "ipAddress") String ipAddress
    ) {
        String deletedIp = redisIpAccessMonitorService.deleteAbnormalAccessIp(request, ipAddress);
        return ApiResponse.success(deletedIp);
    }

    @Operation(summary = "[S] 비정상 접근 IP 기록 초기화", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "지속적인 비정상 접근으로 인해 차단된 IP를 모두 삭제")
    @Secured({ "ROLE_SUPER" })
    @DeleteMapping("/abnormal-access/clear")
    public ApiResponse<List<RedisIpAccessMonitor>> clearAbnormalAccessBlacklist(
            HttpServletRequest request
    ) {
        List<RedisIpAccessMonitor> ipAccessMonitors = redisIpAccessMonitorService.clearAbnormalAccessIps(request);
        return ApiResponse.success(ipAccessMonitors);
    }

}
