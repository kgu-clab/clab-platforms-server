package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.AccountLockManagementUseCase;
import page.clab.api.domain.login.application.port.in.AuthenticatorUseCase;
import page.clab.api.domain.login.application.port.in.LoginAttemptLogManagementUseCase;
import page.clab.api.domain.login.application.port.in.LoginUseCase;
import page.clab.api.domain.login.application.port.in.RedisTokenManagementUseCase;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.login.exception.LoginFailedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("twoFactorAuthenticationService")
public class TwoFactorAuthenticationService implements LoginUseCase {

    private final AccountLockManagementUseCase accountLockManagementUseCase;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final LoginAttemptLogManagementUseCase loginAttemptLogManagementUseCase;
    private final RedisTokenManagementUseCase redisTokenManagementUseCase;
    private final AuthenticatorUseCase authenticatorUseCase;
    private final SlackService slackService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto) throws LoginFailedException, MemberLockedException {
        String memberId = twoFactorAuthenticationRequestDto.getMemberId();
        MemberLoginInfoDto loginMember = memberInfoRetrievalUseCase.getMemberLoginInfoById(memberId);
        String totp = twoFactorAuthenticationRequestDto.getTotp();

        accountLockManagementUseCase.handleAccountLockInfo(memberId);
        verifyTwoFactorAuthentication(memberId, totp, request);

        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        sendAdminLoginNotification(request, loginMember);
        String header = TokenHeader.create(tokenInfo).toJson();
        return LoginResult.create(header, true);
    }

    private void verifyTwoFactorAuthentication(String memberId, String totp, HttpServletRequest request) throws MemberLockedException, LoginFailedException {
        if (!authenticatorUseCase.isAuthenticatorValid(memberId, totp)) {
            loginAttemptLogManagementUseCase.logLoginAttempt(request, memberId, LoginAttemptResult.FAILURE);
            accountLockManagementUseCase.handleLoginFailure(request, memberId);
            throw new LoginFailedException("잘못된 인증번호입니다.");
        }
        loginAttemptLogManagementUseCase.logLoginAttempt(request, memberId, LoginAttemptResult.TOTP);
    }

    private TokenInfo generateAndSaveToken(MemberLoginInfoDto memberInfo) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(memberInfo.getMemberId(), memberInfo.getRole());
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        redisTokenManagementUseCase.saveToken(memberInfo.getMemberId(), memberInfo.getRole(), tokenInfo, clientIpAddress);
        return tokenInfo;
    }

    private void sendAdminLoginNotification(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        if (loginMember.isSuperAdminRole()) {
            slackService.sendAdminLoginNotification(request, loginMember);
        }
    }

    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFailedException, MemberLockedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String resetAuthenticator(String memberId) {
        return authenticatorUseCase.resetAuthenticator(memberId);
    }

    @Override
    public String revokeToken(String memberId) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public TokenHeader reissueToken(HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public List<String> retrieveCurrentLoggedInUsers() {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
