package page.clab.api.domain.auth.blacklistIp.application.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.blacklistIp.application.port.in.ResetBlacklistIpsUseCase;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.notificationSetting.adapter.out.slack.SlackService;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpResetService implements ResetBlacklistIpsUseCase {

    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;
    private final RemoveBlacklistIpPort removeBlacklistIpPort;
    private final SlackService slackService;

    /**
     * 블랙리스트에 등록된 모든 IP 주소를 초기화합니다.
     *
     * <p>블랙리스트에 등록된 모든 IP 주소를 조회하고 삭제합니다.
     * 삭제 완료 후, Slack을 통해 모든 IP 주소가 제거되었음을 알리는 보안 알림이 전송됩니다.</p>
     *
     * @param request 현재 요청 객체
     * @return 삭제된 블랙리스트 IP 주소 목록
     */
    @Transactional
    @Override
    public List<String> resetBlacklistIps(HttpServletRequest request) {
        List<String> blacklistedIps = retrieveBlacklistIpPort.findAll()
                .stream()
                .map(BlacklistIp::getIpAddress)
                .toList();
        removeBlacklistIpPort.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED,
                "Deleted IP: ALL");
        return blacklistedIps;
    }
}
