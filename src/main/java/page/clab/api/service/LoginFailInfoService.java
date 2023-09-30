package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.UserLockedException;
import page.clab.api.repository.LoginFailInfoRepository;
import page.clab.api.type.entity.LoginFailInfo;
import page.clab.api.type.entity.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginFailInfoService {

    private final UserService userService;

    private final LoginFailInfoRepository loginFailInfoRepository;

    private static final int MAX_LOGIN_FAILURES = 5;

    private static final int LOCK_DURATION_MINUTES = 5;

    public LoginFailInfo createLoginFailInfo(User user) {
        LoginFailInfo loginFailInfo = LoginFailInfo.builder()
                .user(user)
                .loginFailCount(0L)
                .isLock(false)
                .build();
        loginFailInfoRepository.save(loginFailInfo);
        return loginFailInfo;
    }

    public void banUserById(String userId) throws PermissionDeniedException {
        userService.checkUserAdminRole();
        LoginFailInfo loginFailInfo = getLoginFailInfoByUserIdOrThrow(userId);
        if (loginFailInfo != null) {
            loginFailInfo.setIsLock(true);
            loginFailInfo.setLatestTryLoginDate(LocalDateTime.now().plusYears(100));
            loginFailInfoRepository.save(loginFailInfo);
        }
    }

    public void unbanUserById(String userId) throws PermissionDeniedException {
        userService.checkUserAdminRole();
        LoginFailInfo loginFailInfo = getLoginFailInfoByUserIdOrThrow(userId);
        if (loginFailInfo != null) {
            loginFailInfo.setIsLock(false);
            loginFailInfoRepository.save(loginFailInfo);
        }
    }

    public void checkUserLocked(LoginFailInfo loginFailInfo) throws UserLockedException {
        if (loginFailInfo != null && loginFailInfo.getIsLock() && isLockedForDuration(loginFailInfo)) {
            throw new UserLockedException();
        }
    }

    public boolean isLockedForDuration(LoginFailInfo loginFailInfo) {
        LocalDateTime unlockTime = loginFailInfo.getLatestTryLoginDate().plusMinutes(LOCK_DURATION_MINUTES);
        return LocalDateTime.now().isBefore(unlockTime);
    }

    public void resetLoginFailInfo(LoginFailInfo loginFailInfo) {
        if (loginFailInfo != null) {
            loginFailInfo.setLoginFailCount(0L);
            loginFailInfo.setIsLock(false);
            loginFailInfoRepository.save(loginFailInfo);
        }
    }

    public void updateLoginFailInfo(String userId) throws LoginFaliedException {
        LoginFailInfo loginFailInfo = getLoginFailInfoByUserIdOrThrow(userId);
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

    public LoginFailInfo getLoginFailInfoByUserIdOrThrow(String userId) {
        return loginFailInfoRepository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

}
