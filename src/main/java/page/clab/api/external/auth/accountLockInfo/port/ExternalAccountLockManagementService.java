package page.clab.api.external.auth.accountLockInfo.port;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.ManageAccountLockUseCase;
import page.clab.api.domain.auth.login.application.exception.LoginFailedException;
import page.clab.api.domain.auth.login.application.exception.MemberLockedException;
import page.clab.api.external.auth.accountLockInfo.application.ExternalManageAccountLockUseCase;

@Service
@RequiredArgsConstructor
public class ExternalAccountLockManagementService implements ExternalManageAccountLockUseCase {

    private final ManageAccountLockUseCase manageAccountLockUseCase;

    @Override
    public void handleAccountLockInfo(String memberId) throws LoginFailedException, MemberLockedException {
        manageAccountLockUseCase.handleAccountLockInfo(memberId);
    }

    @Override
    public void handleLoginFailure(HttpServletRequest request, String memberId) throws LoginFailedException, MemberLockedException {
        manageAccountLockUseCase.handleLoginFailure(request, memberId);
    }
}
