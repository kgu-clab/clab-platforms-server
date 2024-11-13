package page.clab.api.external.auth.accountLockInfo.port;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class ExternalAccountLockManagementService implements ExternalManageAccountLockUseCase {

    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${security.login-attempt.max-failures}")
    private int maxLoginFailures;

    @Value("${security.login-attempt.lock-duration-minutes}")
    private int lockDurationMinutes;

    /**
     * 계정 잠금 정보를 처리합니다.
     *
     * <p>해당 멤버가 존재하는지 확인하고, 계정 잠금 상태를 검증한 후,
     * 계정을 잠금 해제하고 업데이트된 계정 잠금 정보를 저장합니다.</p>
     *
     * @param memberId 잠금 해제하려는 멤버의 ID
     * @throws MemberLockedException 계정이 현재 잠겨 있을 경우 예외 발생
     * @throws LoginFailedException  멤버가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    @Override
    public void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFailedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.unlockAccount();
        registerAccountLockInfoPort.save(accountLockInfo);
    }

    /**
     * 로그인 실패를 처리하고 계정 잠금을 관리합니다.
     *
     * <p>로그인 실패 시 멤버의 존재 여부와 계정 잠금 상태를 확인합니다.
     * 로그인 실패 횟수를 증가시키며, 설정된 최대 실패 횟수에 도달하면 계정을 잠그고 Slack에 보안 알림을 전송합니다.</p>
     *
     * @param request  현재 HTTP 요청 객체
     * @param memberId 로그인 실패를 기록할 멤버의 ID
     * @throws MemberLockedException 계정이 현재 잠겨 있을 경우 예외 발생
     * @throws LoginFailedException  멤버가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    @Override
    public void handleLoginFailure(HttpServletRequest request, String memberId)
            throws MemberLockedException, LoginFailedException {
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
            String additionalMessage = "로그인 실패 횟수 초과로 계정이 잠겼습니다.";
            eventPublisher.publishEvent(
                    new NotificationEvent(this, SecurityAlertType.REPEATED_LOGIN_FAILURES, request, additionalMessage));
        }
    }
}
