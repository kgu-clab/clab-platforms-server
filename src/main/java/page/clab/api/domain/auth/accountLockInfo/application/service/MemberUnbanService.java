package page.clab.api.domain.auth.accountLockInfo.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.UnbanMemberUseCase;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberUnbanService implements UnbanMemberUseCase {

    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final SlackService slackService;

    /**
     * 차단된 멤버를 해제합니다.
     *
     * <p>해당 멤버의 계정 잠금 정보를 조회하고 해제합니다.
     * 해제된 정보는 저장되며, Slack에 해제 알림이 전송됩니다.</p>
     *
     * @param request 현재 요청 객체
     * @param memberId 해제할 멤버의 ID
     * @return 업데이트된 계정 잠금 정보의 ID
     */
    @Transactional
    @Override
    public Long unbanMember(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.unban();
        sendSlackUnbanNotification(request, memberId);
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

    private void sendSlackUnbanNotification(HttpServletRequest request, String memberId) {
        String memberName = externalRetrieveMemberUseCase.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_UNBANNED, "ID: " + memberId + ", Name: " + memberName);
    }
}
