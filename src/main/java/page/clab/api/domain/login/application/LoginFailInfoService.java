package page.clab.api.domain.login.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.dao.LoginFailInfoRepository;
import page.clab.api.domain.login.domain.LoginFailInfo;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginFailInfoService {

    private final MemberService memberService;

    private final LoginFailInfoRepository loginFailInfoRepository;

    private static final int MAX_LOGIN_FAILURES = 5;

    private static final int LOCK_DURATION_MINUTES = 5;

    public LoginFailInfo createLoginFailInfo(Member member) {
        LoginFailInfo loginFailInfo = LoginFailInfo.builder()
                .member(member)
                .loginFailCount(0L)
                .isLock(false)
                .build();
        loginFailInfoRepository.save(loginFailInfo);
        return loginFailInfo;
    }

    public Long banMemberById(String memberId) {
        LoginFailInfo loginFailInfo = getLoginFailInfoByMemberIdOrThrow(memberId);
        if (loginFailInfo == null) {
            return null;
        }
        loginFailInfo.setIsLock(true);
        loginFailInfo.setLatestTryLoginDate(LocalDateTime.now().plusYears(100));
        return loginFailInfoRepository.save(loginFailInfo).getId();
    }

    public Long unbanMemberById(String memberId) {
        LoginFailInfo loginFailInfo = getLoginFailInfoByMemberIdOrThrow(memberId);
        if (loginFailInfo != null) {
            loginFailInfo.setIsLock(false);
            return loginFailInfoRepository.save(loginFailInfo).getId();
        }
        return loginFailInfo.getId();
    }

    public void handleLoginFailInfo(String memberId) throws MemberLockedException {
        LoginFailInfo loginFailInfo = getLoginFailInfoByMemberId(memberId);
        if (loginFailInfo == null) {
            Member member = memberService.getMemberByIdOrThrow(memberId);
            loginFailInfo = createLoginFailInfo(member);
        }
        if (isMemberLocked(loginFailInfo)) {
            throw new MemberLockedException();
        }
        resetLoginFailInfo(loginFailInfo);
        loginFailInfoRepository.save(loginFailInfo);
    }

    public boolean isMemberLocked(LoginFailInfo loginFailInfo) {
        if (loginFailInfo != null && loginFailInfo.getIsLock() && isLockedForDuration(loginFailInfo)) {
            return true;
        }
        return false;
    }

    public boolean isLockedForDuration(LoginFailInfo loginFailInfo) {
        LocalDateTime unlockTime = loginFailInfo.getLatestTryLoginDate().plusMinutes(LOCK_DURATION_MINUTES);
        return LocalDateTime.now().isBefore(unlockTime);
    }

    public void resetLoginFailInfo(LoginFailInfo loginFailInfo) {
        if (loginFailInfo != null) {
            loginFailInfo.setLoginFailCount(0L);
            loginFailInfo.setIsLock(false);
        }
    }

    public void updateLoginFailInfo(String memberId) throws LoginFaliedException {
        LoginFailInfo loginFailInfo = getLoginFailInfoByMemberId(memberId);
        if (loginFailInfo != null) {
            incrementFailCountAndLock(loginFailInfo);
        }
        throw new LoginFaliedException();
    }

    public void incrementFailCountAndLock(LoginFailInfo loginFailInfo) {
        loginFailInfo.setLoginFailCount(loginFailInfo.getLoginFailCount() + 1);
        if (loginFailInfo.getLoginFailCount() >= MAX_LOGIN_FAILURES) {
            if (loginFailInfo.getIsLock().equals(false)) {
                loginFailInfo.setLatestTryLoginDate(LocalDateTime.now());
                loginFailInfo.setIsLock(true);
            }
        }
        loginFailInfoRepository.save(loginFailInfo);
    }

    public LoginFailInfo getLoginFailInfoByMemberIdOrThrow(String memberId) {
        return loginFailInfoRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

    public LoginFailInfo getLoginFailInfoByMemberId(String memberId) {
        return loginFailInfoRepository.findByMember_Id(memberId).orElse(null);
    }

}
