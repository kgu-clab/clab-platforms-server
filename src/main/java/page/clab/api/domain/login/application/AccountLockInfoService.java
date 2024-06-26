package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.dao.AccountLockInfoRepository;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.login.dto.response.AccountLockInfoResponseDto;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLockInfoService {

    private final MemberLookupService memberLookupService;

    private final SlackService slackService;

    private final RedisTokenService redisTokenService;

    private final AccountLockInfoRepository accountLockInfoRepository;

    @Value("${security.login-attempt.max-failures}")
    private int maxLoginFailures;

    @Value("${security.login-attempt.lock-duration-minutes}")
    private int lockDurationMinutes;

    @Transactional
    public Long banMemberById(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.banPermanently();
        redisTokenService.deleteRedisTokenByMemberId(memberId);
        sendSlackBanNotification(request, memberId);
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    @Transactional
    public Long unbanMemberById(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.unban();
        sendSlackUnbanNotification(request, memberId);
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AccountLockInfoResponseDto> getBanMembers(Pageable pageable) {
        LocalDateTime banDate = LocalDateTime.of(9999, 12, 31, 23, 59);
        Page<AccountLockInfo> banMembers = accountLockInfoRepository.findByLockUntil(banDate, pageable);
        return new PagedResponseDto<>(banMembers.map(accountLockInfo -> {
            String memberName = memberLookupService.getMemberBasicInfoById(accountLockInfo.getMemberId()).getMemberName();
            return AccountLockInfoResponseDto.toDto(accountLockInfo, memberName);
        }));
    }

    @Transactional
    public void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFaliedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.unlockAccount();
        accountLockInfoRepository.save(accountLockInfo);
    }

    @Transactional
    public void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFaliedException {
        ensureMemberExists(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.incrementLoginFailCount();
        if (accountLockInfo.shouldBeLocked(maxLoginFailures)) {
            accountLockInfo.lockAccount(lockDurationMinutes);
            sendSlackLoginFailureNotification(request, memberId);
        }
        accountLockInfoRepository.save(accountLockInfo);
    }

    public AccountLockInfo createAccountLockInfo(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return accountLockInfoRepository.findByMemberId(memberId)
                .orElseGet(() -> createAccountLockInfo(memberId));
    }

    private void ensureMemberExists(String memberId) throws LoginFaliedException {
        if (memberLookupService.getMemberById(memberId) == null) {
            throw new LoginFaliedException();
        }
    }

    private void validateAccountLockStatus(AccountLockInfo accountLockInfo) throws MemberLockedException {
        if (accountLockInfo.isCurrentlyLocked()) {
            throw new MemberLockedException();
        }
    }

    private void sendSlackBanNotification(HttpServletRequest request, String memberId) {
        String memberName = memberLookupService.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + memberId + ", Name: " + memberName);
    }

    private void sendSlackUnbanNotification(HttpServletRequest request, String memberId) {
        String memberName = memberLookupService.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_UNBANNED, "ID: " + memberId + ", Name: " + memberName);
    }

    private void sendSlackLoginFailureNotification(HttpServletRequest request, String memberId) {
        String memberName = memberLookupService.getMemberBasicInfoById(memberId).getMemberName();
        if (memberLookupService.getMemberDetailedInfoById(memberId).isAdminRole()) {
            request.setAttribute("member", memberId + " " + memberName);
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.REPEATED_LOGIN_FAILURES, "로그인 실패 횟수 초과로 계정이 잠겼습니다.");
        }
    }

}
