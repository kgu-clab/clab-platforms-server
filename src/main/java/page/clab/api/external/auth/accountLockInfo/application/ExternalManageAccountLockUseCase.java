package page.clab.api.external.auth.accountLockInfo.application;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.auth.login.application.exception.LoginFailedException;
import page.clab.api.domain.auth.login.application.exception.MemberLockedException;

public interface ExternalManageAccountLockUseCase {

    void handleAccountLockInfo(String memberId) throws LoginFailedException, MemberLockedException;

    void handleLoginFailure(HttpServletRequest request, String memberId)
        throws LoginFailedException, MemberLockedException;
}
