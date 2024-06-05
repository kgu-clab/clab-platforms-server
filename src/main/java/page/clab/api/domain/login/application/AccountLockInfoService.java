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
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLockInfoService {

    private final MemberService memberService;

    private final SlackService slackService;

    private final RedisTokenService redisTokenService;

    private final AccountLockInfoRepository accountLockInfoRepository;

    @Value("${security.login-attempt.max-failures}")
    private int maxLoginFailures;

    @Value("${security.login-attempt.lock-duration-minutes}")
    private int lockDurationMinutes;

    @Transactional
    public Long banMemberById(HttpServletRequest request, String memberId) {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(member);
        accountLockInfo.banPermanently();
        redisTokenService.deleteRedisTokenByMemberId(memberId);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + member.getId() + ", Name: " + member.getName());
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    @Transactional
    public Long unbanMemberById(HttpServletRequest request, String memberId) {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(member);
        accountLockInfo.unban();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_UNBANNED, "ID: " + member.getId() + ", Name: " + member.getName());
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<AccountLockInfoResponseDto> getBanMembers(Pageable pageable) {
        LocalDateTime banDate = LocalDateTime.of(9999, 12, 31, 23, 59);
        Page<AccountLockInfo> banMembers = accountLockInfoRepository.findByLockUntil(banDate, pageable);
        return new PagedResponseDto<>(banMembers.map(AccountLockInfoResponseDto::toDto));
    }

    @Transactional
    public void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFaliedException {
        AccountLockInfo accountLockInfo = ensureAccountLockInfoForMemberId(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.unlockAccount();
        accountLockInfoRepository.save(accountLockInfo);
    }

    @Transactional
    public void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFaliedException {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfoForMemberId(memberId);
        validateAccountLockStatus(accountLockInfo);
        accountLockInfo.incrementLoginFailCount();
        if (accountLockInfo.shouldBeLocked(maxLoginFailures)) {
            accountLockInfo.lockAccount(lockDurationMinutes);
            sendSlackMessage(request, member);
        }
        accountLockInfoRepository.save(accountLockInfo);
    }

    public AccountLockInfo createAccountLockInfo(Member member) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(member);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }

    private AccountLockInfo ensureAccountLockInfo(Member member) {
        return accountLockInfoRepository.findByMember(member)
                .orElseGet(() -> createAccountLockInfo(member));
    }

    private AccountLockInfo ensureAccountLockInfoForMemberId(String memberId) throws LoginFaliedException {
        Member member = memberService.getMemberByIdOrThrowLoginFailed(memberId);
        return ensureAccountLockInfo(member);
    }

    private void validateAccountLockStatus(AccountLockInfo accountLockInfo) throws MemberLockedException {
        if (accountLockInfo.isCurrentlyLocked()) {
            throw new MemberLockedException();
        }
    }

    private void sendSlackMessage(HttpServletRequest request, Member member) {
        if (member.isAdminRole()) {
            request.setAttribute("member", member.getId() + " " + member.getName());
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.REPEATED_LOGIN_FAILURES, "로그인 실패 횟수 초과로 계정이 잠겼습니다.");
        }
    }

}
