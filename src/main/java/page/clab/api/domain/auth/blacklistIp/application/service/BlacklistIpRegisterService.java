package page.clab.api.domain.auth.blacklistIp.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.blacklistIp.application.dto.mapper.BlacklistIpDtoMapper;
import page.clab.api.domain.auth.blacklistIp.application.dto.request.BlacklistIpRequestDto;
import page.clab.api.domain.auth.blacklistIp.application.port.in.RegisterBlacklistIpUseCase;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RegisterBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.notificationSetting.adapter.out.slack.SlackService;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpRegisterService implements RegisterBlacklistIpUseCase {

    private final RegisterBlacklistIpPort registerBlacklistIpPort;
    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;
    private final SlackService slackService;
    private final BlacklistIpDtoMapper mapper;

    /**
     * 지정된 IP 주소를 블랙리스트에 등록합니다.
     *
     * <p>해당 IP 주소가 이미 블랙리스트에 존재하는지 확인하고,
     * 존재하지 않을 경우 새롭게 등록합니다. 새로운 IP가 등록되면 Slack을 통해 보안 알림이 전송됩니다.</p>
     *
     * @param request    현재 요청 객체
     * @param requestDto 블랙리스트에 추가할 IP 주소 정보를 담은 DTO
     * @return 기존에 존재하거나 새로 추가된 블랙리스트 IP 주소
     */
    @Transactional
    @Override
    public String registerBlacklistIp(HttpServletRequest request, BlacklistIpRequestDto requestDto) {
        String ipAddress = requestDto.getIpAddress();
        return retrieveBlacklistIpPort.findByIpAddress(ipAddress)
                .map(BlacklistIp::getIpAddress)
                .orElseGet(() -> {
                    BlacklistIp blacklistIp = mapper.fromDto(requestDto);
                    registerBlacklistIpPort.save(blacklistIp);
                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED,
                            "Added IP: " + ipAddress);
                    return ipAddress;
                });
    }
}
