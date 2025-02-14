package page.clab.api.domain.auth.accountLockInfo.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.BanMemberUseCase;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberBanService implements BanMemberUseCase {

    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalManageRedisTokenUseCase externalManageRedisTokenUseCase;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 멤버를 영구적으로 차단합니다.
     *
     * <p>해당 멤버의 계정 잠금 정보를 조회하고, 없으면 새로 생성합니다.
     * Redis에 저장된 해당 멤버의 인증 토큰을 삭제하며, Slack에 밴 알림을 전송합니다.</p>
     *
     * @param request  현재 요청 객체
     * @param memberId 차단할 멤버의 ID
     * @return 저장된 계정 잠금 정보의 ID
     */
    @Transactional
    @Override
    public Long banMember(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.banPermanently();
        externalManageRedisTokenUseCase.deleteByMemberId(memberId);
        sendSlackBanNotification(request, memberId);
        return registerAccountLockInfoPort.save(accountLockInfo).getId();
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return retrieveAccountLockInfoPort.findByMemberId(memberId)
            .orElseGet(() -> createAccountLockInfo(memberId));
    }

    private AccountLockInfo createAccountLockInfo(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        registerAccountLockInfoPort.save(accountLockInfo);
        return accountLockInfo;
    }

    private void sendSlackBanNotification(HttpServletRequest request, String memberId) {
        String memberName = externalRetrieveMemberUseCase.getMemberBasicInfoById(memberId).getMemberName();
        String memberBannedMessage = "ID: " + memberId + ", Name: " + memberName;
        eventPublisher.publishEvent(
            new NotificationEvent(this, SecurityAlertType.MEMBER_BANNED, request, memberBannedMessage));
    }
}
