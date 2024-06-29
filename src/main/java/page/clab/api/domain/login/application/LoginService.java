package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;

import java.util.List;

public interface LoginService {
    LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFaliedException, MemberLockedException;
    LoginResult authenticator(HttpServletRequest request, TwoFactorAuthenticationRequestDto requestDto) throws LoginFaliedException, MemberLockedException;
    String resetAuthenticator(String memberId);
    String revoke(String memberId);
    TokenHeader reissue(HttpServletRequest request);
    List<String> getCurrentLoggedInUsers();
}
