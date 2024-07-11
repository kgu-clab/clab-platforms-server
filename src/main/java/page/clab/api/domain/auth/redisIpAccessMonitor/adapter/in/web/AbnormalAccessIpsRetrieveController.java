package page.clab.api.domain.auth.redisIpAccessMonitor.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.RetrieveAbnormalAccessIpsUseCase;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/ip-access-monitor")
@RequiredArgsConstructor
@Tag(name = "Authentication - Abnormal Access IP", description = "비정상 접근 IP")
public class AbnormalAccessIpsRetrieveController {

    private final RetrieveAbnormalAccessIpsUseCase retrieveAbnormalAccessIpsUseCase;

    @Operation(summary = "[S] 비정상 접근으로 인한 차단 IP 목록 조회", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "지속적인 비정상 접근으로 인해 Redis에 추가된 IP를 조회")
    @Secured({ "ROLE_SUPER" })
    @GetMapping("/abnormal-access")
    public ApiResponse<PagedResponseDto<RedisIpAccessMonitor>> retrieveAbnormalAccessBlacklistIps(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<RedisIpAccessMonitor> abnormalAccessBlacklistIps = retrieveAbnormalAccessIpsUseCase.retrieveAbnormalAccessIps(pageable);
        return ApiResponse.success(abnormalAccessBlacklistIps);
    }
}
