package page.clab.api.domain.auth.blacklistIp.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.blacklistIp.application.port.in.RemoveBlacklistIpUseCase;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpRemoveService implements RemoveBlacklistIpUseCase {

    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;
    private final RemoveBlacklistIpPort removeBlacklistIpPort;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 지정된 IP 주소를 블랙리스트에서 제거합니다.
     *
     * <p>블랙리스트에 등록된 IP 주소 정보를 조회하고 해당 정보를 삭제합니다.
     * 삭제가 완료되면 Slack을 통해 보안 알림이 전송됩니다.</p>
     *
     * @param request   현재 요청 객체
     * @param ipAddress 제거할 블랙리스트 IP 주소
     * @return 삭제된 블랙리스트 IP 주소
     */
    @Transactional
    @Override
    public String removeBlacklistIp(HttpServletRequest request, String ipAddress) {
        BlacklistIp blacklistIp = retrieveBlacklistIpPort.getByIpAddress(ipAddress);
        removeBlacklistIpPort.delete(blacklistIp);

        String blacklistRemovedMessage = "Deleted IP: " + ipAddress;
        eventPublisher.publishEvent(
            new NotificationEvent(this, SecurityAlertType.BLACKLISTED_IP_REMOVED, request,
                blacklistRemovedMessage));

        return blacklistIp.getIpAddress();
    }
}
