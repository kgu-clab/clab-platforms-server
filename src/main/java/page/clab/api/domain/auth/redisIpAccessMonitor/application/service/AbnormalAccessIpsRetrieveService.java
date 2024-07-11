package page.clab.api.domain.auth.redisIpAccessMonitor.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.RetrieveAbnormalAccessIpsUseCase;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RetrieveIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AbnormalAccessIpsRetrieveService implements RetrieveAbnormalAccessIpsUseCase {

    private final RetrieveIpAccessMonitorPort retrieveIpAccessMonitorPort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<RedisIpAccessMonitor> retrieveAbnormalAccessIps(Pageable pageable) {
        List<RedisIpAccessMonitor> allMonitors = retrieveIpAccessMonitorPort.findAll();
        List<RedisIpAccessMonitor> filteredMonitors = allMonitors.stream()
                .filter(Objects::nonNull)
                .filter(RedisIpAccessMonitor::isBlocked)
                .sorted(Comparator.comparing(RedisIpAccessMonitor::getLastAttempt).reversed())
                .toList();
        return new PagedResponseDto<>(filteredMonitors, pageable, filteredMonitors.size());
    }
}
