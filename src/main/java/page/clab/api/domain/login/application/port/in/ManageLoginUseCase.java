package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.application.dto.response.LoginResult;
import page.clab.api.domain.login.application.dto.response.TokenHeader;
import page.clab.api.domain.login.application.exception.LoginFailedException;
import page.clab.api.domain.login.application.exception.MemberLockedException;

import java.util.List;

public interface ManageLoginUseCase {
    LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFailedException, MemberLockedException;

    LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto requestDto) throws LoginFailedException, MemberLockedException;

    String resetAuthenticator(String memberId);

    String revokeToken(String memberId);

    TokenHeader reissueToken(HttpServletRequest request);

    List<String> retrieveCurrentLoggedInUsers();
}