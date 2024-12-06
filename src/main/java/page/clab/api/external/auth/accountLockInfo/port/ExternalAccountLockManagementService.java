package page.clab.api.external.auth.accountLockInfo.port;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;
import page.clab.api.domain.auth.login.application.exception.LoginFailedException;
import page.clab.api.domain.auth.login.application.exception.MemberLockedException;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.external.auth.accountLockInfo.application.ExternalManageAccountLockUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class ExternalAccountLockManagementService implements ExternalManageAccountLockUseCase {

    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final SlackService slackService;

    @Value("${security.login-attempt.max-failures}")
    private int maxLoginFailures;

    @Value("${security.login-attempt.lock-duration-minutes}")
    private int lockDurationMinutes;

    @Transactional
    @Override
    public void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFailedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.unlockAccount();
        registerAccountLockInfoPort.save(accountLockInfo);
    }

    @Transactional
    @Override
    public void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFailedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.incrementLoginFailCount();
        if (accountLockInfo.shouldBeLocked(maxLoginFailures)) {
            accountLockInfo.lockAccount(lockDurationMinutes);
            sendSlackLoginFailureNotification(request, memberId);
        }
        registerAccountLockInfoPort.save(accountLockInfo);
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return retrieveAccountLockInfoPort.findByMemberId(memberId)
                .orElseGet(() -> registerAccountLockInfoPort.save(AccountLockInfo.create(memberId)));
    }

    private void ensureMemberExists(String memberId) throws LoginFailedException {
        if (!externalRetrieveMemberUseCase.existsById(memberId)) {
            throw new LoginFailedException();
        }
    }

    private void validateAccountLockStatus(AccountLockInfo accountLockInfo) throws MemberLockedException {
        if (accountLockInfo.isCurrentlyLocked()) {
            throw new MemberLockedException();
        }
    }

    private void sendSlackLoginFailureNotification(HttpServletRequest request, String memberId) {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getMemberDetailedInfoById(memberId);
        String memberName = memberInfo.getMemberName();
        if (memberInfo.isAdminRole()) {
            request.setAttribute("member", memberId + " " + memberName);
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.REPEATED_LOGIN_FAILURES, "로그인 실패 횟수 초과로 계정이 잠겼습니다.");
        }
    }
}
