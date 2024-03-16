package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public AccountLockInfo createAccountLockInfo(Member member) {
        AccountLockInfo accountLockInfo = new AccountLockInfo(null, member, 0L, false, null);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }

    public Long banMemberById(HttpServletRequest request, String memberId) {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(member);
        accountLockInfo.banPermanently();
        redisTokenService.deleteRedisTokenByMemberId(memberId);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + member.getId() + ", Name: " + member.getName());
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    public Long unbanMemberById(HttpServletRequest request, String memberId) {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(member);
        accountLockInfo.unban();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_UNBANNED, "ID: " + member.getId() + ", Name: " + member.getName());
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    public PagedResponseDto<AccountLockInfoResponseDto> getBanList(Pageable pageable) {
        LocalDateTime banDate = LocalDateTime.of(9999, 12, 31, 23, 59);
        Page<AccountLockInfo> banList = accountLockInfoRepository.findByLockUntil(banDate, pageable);
        return new PagedResponseDto<>(banList.map(AccountLockInfoResponseDto::of));
    }

    public void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFaliedException {
        Member member = memberService.getMemberByIdOrThrowLoginFailed(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(member);
        if (isAccountLocked(accountLockInfo)) {
            throw new MemberLockedException();
        }
        resetAccountLockInfo(accountLockInfo);
    }

    private AccountLockInfo ensureAccountLockInfo(Member member) {
        return accountLockInfoRepository.findByMember(member)
                .orElseGet(() -> createAccountLockInfo(member));
    }

    private boolean isAccountLocked(AccountLockInfo accountLockInfo) {
        return accountLockInfo.getIsLock() && accountLockInfo.getLockUntil().isAfter(LocalDateTime.now());
    }

    public void resetAccountLockInfo(AccountLockInfo accountLockInfo) {
        accountLockInfo.setLoginFailCount(0L);
        accountLockInfo.setIsLock(false);
        accountLockInfo.setLockUntil(null);
        accountLockInfoRepository.save(accountLockInfo);
    }

    public void handleLoginFailure(HttpServletRequest request, String memberId) {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(member);
        accountLockInfo.incrementLoginFailCount();
        if (accountLockInfo.shouldBeLocked(maxLoginFailures)) {
            accountLockInfo.lockAccount(LocalDateTime.now().plusMinutes(lockDurationMinutes));
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.REPEATED_LOGIN_FAILURES,
                    "[" + accountLockInfo.getMember().getId() + "/" + accountLockInfo.getMember().getName() + "]" + " 로그인 실패 횟수 초과로 계정이 잠겼습니다.");
        }
        accountLockInfoRepository.save(accountLockInfo);
    }

}
