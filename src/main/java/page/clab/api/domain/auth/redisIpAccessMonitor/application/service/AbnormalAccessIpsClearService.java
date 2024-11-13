package page.clab.api.domain.auth.redisIpAccessMonitor.application.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.ClearAbnormalAccessIpsUseCase;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.ClearIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RetrieveIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.notificationSetting.adapter.out.slack.SlackService;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class AbnormalAccessIpsClearService implements ClearAbnormalAccessIpsUseCase {

    private final ClearIpAccessMonitorPort clearIpAccessMonitorPort;
    private final RetrieveIpAccessMonitorPort retrieveIpAccessMonitorPort;
    private final SlackService slackService;

    @Override
    @Transactional
    public List<RedisIpAccessMonitor> clearAbnormalAccessIps(HttpServletRequest request) {
        List<RedisIpAccessMonitor> ipAccessMonitors = retrieveIpAccessMonitorPort.findAll();
        clearIpAccessMonitorPort.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED,
                "Deleted IP: ALL");
        return ipAccessMonitors;
    }
}
