package page.clab.api.domain.auth.redisIpAccessMonitor.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in.RemoveAbnormalAccessIpUseCase;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RemoveIpAccessMonitorPort;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class AbnormalAccessIpRemoveService implements RemoveAbnormalAccessIpUseCase {

    private final RemoveIpAccessMonitorPort removeIpAccessMonitorPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public String removeAbnormalAccessIp(HttpServletRequest request, String ipAddress) {
        removeIpAccessMonitorPort.deleteById(ipAddress);
        String abnormalAccessIpDeletedMessage = "Deleted IP: " + ipAddress;
        eventPublisher.publishEvent(
                new NotificationEvent(this, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED, request,
                        abnormalAccessIpDeletedMessage));
        return ipAddress;
    }
}
