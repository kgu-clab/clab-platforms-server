package page.clab.api.domain.auth.accountLockInfo.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.auth.login.application.exception.LoginFailedException;
import page.clab.api.domain.auth.login.application.exception.MemberLockedException;

public interface ManageAccountLockUseCase {
    void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFailedException;

    void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFailedException;
}
