package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.exception.LoginFailedException;
import page.clab.api.domain.login.exception.MemberLockedException;

public interface ManageAccountLockUseCase {
    void handleAccountLockInfo(String memberId) throws MemberLockedException, LoginFailedException;
    void handleLoginFailure(HttpServletRequest request, String memberId) throws MemberLockedException, LoginFailedException;
}
