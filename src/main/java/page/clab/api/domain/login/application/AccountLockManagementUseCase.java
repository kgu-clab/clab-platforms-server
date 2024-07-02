package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;

public interface AccountLockManagementUseCase {
    void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFaliedException;
    void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFaliedException;
}