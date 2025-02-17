package page.clab.api.external.auth.accountLockInfo.application;

import jakarta.servlet.http.HttpServletRequest;

public interface ExternalManageAccountLockUseCase {

    void handleAccountLockInfo(String memberId);

    void handleLoginFailure(HttpServletRequest request, String memberId);
}
