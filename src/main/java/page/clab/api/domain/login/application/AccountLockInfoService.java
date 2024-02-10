package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLockInfoService {

    private final MemberService memberService;

    private final SlackService slackService;

    private final AccountLockInfoRepository accountLockInfoRepository;

    private static final int MAX_LOGIN_FAILURES = 5;

    private static final int LOCK_DURATION_MINUTES = 5;

    public AccountLockInfo createAccountLockInfo(Member member) {
        AccountLockInfo accountLockInfo = AccountLockInfo.builder()
                .member(member)
                .loginFailCount(0L)
                .isLock(false)
                .build();
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }

    public Long banMemberById(String memberId) {
        Member member = memberService.getMemberById(memberId);
        AccountLockInfo accountLockInfo = getAccountLockInfoByMemberId(memberId);
        if (accountLockInfo == null) {
            accountLockInfo = createAccountLockInfo(member);
        }
        accountLockInfo.setIsLock(true);
        accountLockInfo.setLockUntil(LocalDateTime.of(9999, 12, 31, 23, 59));
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    public Long unbanMemberById(String memberId) {
        AccountLockInfo accountLockInfo = getAccountLockInfoByMemberIdOrThrow(memberId);
        if (accountLockInfo != null) {
            accountLockInfo.setIsLock(false);
            accountLockInfo.setLockUntil(null);
            return accountLockInfoRepository.save(accountLockInfo).getId();
        }
        return accountLockInfo.getId();
    }

    public PagedResponseDto<AccountLockInfoResponseDto> getBanList(Pageable pageable) {
        LocalDateTime banDate = LocalDateTime.of(9999, 12, 31, 23, 59);
        Page<AccountLockInfo> banList = accountLockInfoRepository.findByLockUntil(banDate, pageable);
        return new PagedResponseDto<>(banList.map(AccountLockInfoResponseDto::of));
    }

    public void handleAccountLockInfo(String memberId) throws MemberLockedException {
        AccountLockInfo accountLockInfo = getAccountLockInfoByMemberId(memberId);
        if (accountLockInfo == null) {
            Member member = memberService.getMemberByIdOrThrow(memberId);
            accountLockInfo = createAccountLockInfo(member);
        }
        if (isMemberLocked(accountLockInfo)) {
            throw new MemberLockedException();
        }
        resetAccountLockInfo(accountLockInfo);
        accountLockInfoRepository.save(accountLockInfo);
    }

    public boolean isMemberLocked(AccountLockInfo accountLockInfo) {
        if (accountLockInfo != null && accountLockInfo.getIsLock() && isLockedForDuration(accountLockInfo)) {
            return true;
        }
        return false;
    }

    public boolean isLockedForDuration(AccountLockInfo accountLockInfo) {
        LocalDateTime unlockTime = accountLockInfo.getLockUntil();
        return LocalDateTime.now().isBefore(unlockTime);
    }

    public void resetAccountLockInfo(AccountLockInfo accountLockInfo) {
        if (accountLockInfo != null) {
            accountLockInfo.setLoginFailCount(0L);
            accountLockInfo.setIsLock(false);
        }
    }

    public void updateAccountLockInfo(HttpServletRequest request, String memberId) throws LoginFaliedException {
        AccountLockInfo accountLockInfo = getAccountLockInfoByMemberId(memberId);
        if ((accountLockInfo == null)) {
            createAccountLockInfo(memberService.getMemberByIdOrThrow(memberId));
        } else {
            incrementFailCountAndLock(request, accountLockInfo);
        }
        throw new LoginFaliedException();
    }

    public void incrementFailCountAndLock(HttpServletRequest request, AccountLockInfo accountLockInfo) {
        accountLockInfo.setLoginFailCount(accountLockInfo.getLoginFailCount() + 1);
        if (accountLockInfo.getLoginFailCount() >= MAX_LOGIN_FAILURES) {
            if (accountLockInfo.getIsLock().equals(false)) {
                accountLockInfo.setLockUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
                accountLockInfo.setIsLock(true);
                slackService.sendSecurityAlertNotification(request, SecurityAlertType.REPEATED_LOGIN_FAILURES,
                        "[" + accountLockInfo.getMember().getId() + "/" + accountLockInfo.getMember().getName() + "]" + " 로그인 실패 횟수 초과로 계정이 잠겼습니다.");
            }
        }
        accountLockInfoRepository.save(accountLockInfo);
    }

    public AccountLockInfo getAccountLockInfoByMemberIdOrThrow(String memberId) {
        return accountLockInfoRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

    public AccountLockInfo getAccountLockInfoByMemberId(String memberId) {
        return accountLockInfoRepository.findByMember_Id(memberId).orElse(null);
    }

}
