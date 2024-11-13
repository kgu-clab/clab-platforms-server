package page.clab.api.domain.auth.redisIpAccessMonitor.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.RemoveAbnormalAccessIpUseCase;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RemoveIpAccessMonitorPort;
import page.clab.api.global.common.notificationSetting.adapter.out.slack.SlackService;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class AbnormalAccessIpRemoveService implements RemoveAbnormalAccessIpUseCase {

    private final RemoveIpAccessMonitorPort removeIpAccessMonitorPort;
    private final SlackService slackService;

    @Override
    @Transactional
    public String removeAbnormalAccessIp(HttpServletRequest request, String ipAddress) {
        removeIpAccessMonitorPort.deleteById(ipAddress);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED,
                "Deleted IP: " + ipAddress);
        return ipAddress;
    }
}
