package page.clab.api.domain.auth.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import page.clab.api.domain.auth.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.auth.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.auth.login.application.dto.response.LoginResult;
import page.clab.api.domain.auth.login.application.dto.response.TokenHeader;

public interface ManageLoginUseCase {

    LoginResult login(HttpServletRequest request, LoginRequestDto requestDto);

    LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto requestDto);

    String resetAuthenticator(String memberId);

    TokenHeader reissueToken(HttpServletRequest request);

    List<String> retrieveCurrentLoggedInUsers();

    LoginResult guestLogin(HttpServletRequest request);
}
