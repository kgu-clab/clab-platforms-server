package page.clab.api.domain.auth.accountLockInfo.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.BanMemberUseCase;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberBanService implements BanMemberUseCase {

    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalManageRedisTokenUseCase externalManageRedisTokenUseCase;
    private final SlackService slackService;

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
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + memberId + ", Name: " + memberName);
    }
}
