package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("twoFactorAuthenticationService")
public class TwoFactorAuthenticationService implements LoginService {

    private final AccountLockManagementService accountLockManagementService;

    private final MemberLookupService memberLookupService;

    private final LoginAttemptLogManagementService loginAttemptLogManagementService;

    private final RedisTokenService redisTokenService;

    private final AuthenticatorService authenticatorService;

    private final SlackService slackService;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public LoginResult authenticator(HttpServletRequest request, TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto) throws LoginFaliedException, MemberLockedException {
        String memberId = twoFactorAuthenticationRequestDto.getMemberId();
        MemberLoginInfoDto loginMember = memberLookupService.getMemberLoginInfoById(memberId);
        String totp = twoFactorAuthenticationRequestDto.getTotp();

        accountLockManagementService.handleAccountLockInfo(memberId);
        verifyTwoFactorAuthentication(memberId, totp, request);

        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        sendAdminLoginNotification(request, loginMember);
        String header = TokenHeader.create(tokenInfo).toJson();
        return LoginResult.create(header, true);
    }

    private void verifyTwoFactorAuthentication(String memberId, String totp, HttpServletRequest request) throws MemberLockedException, LoginFaliedException {
        if (!authenticatorService.isAuthenticatorValid(memberId, totp)) {
            loginAttemptLogManagementService.createLoginAttemptLog(request, memberId, LoginAttemptResult.FAILURE);
            accountLockManagementService.handleLoginFailure(request, memberId);
            throw new LoginFaliedException("잘못된 인증번호입니다.");
        }
        loginAttemptLogManagementService.createLoginAttemptLog(request, memberId, LoginAttemptResult.TOTP);
    }

    private TokenInfo generateAndSaveToken(MemberLoginInfoDto memberInfo) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(memberInfo.getMemberId(), memberInfo.getRole());
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        redisTokenService.saveRedisToken(memberInfo.getMemberId(), memberInfo.getRole(), tokenInfo, clientIpAddress);
        return tokenInfo;
    }

    private void sendAdminLoginNotification(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        if (loginMember.isSuperAdminRole()) {
            slackService.sendAdminLoginNotification(request, loginMember);
        }
    }

    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFaliedException, MemberLockedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String resetAuthenticator(String memberId) {
        return authenticatorService.resetAuthenticator(memberId);
    }

    @Override
    public String revoke(String memberId) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public TokenHeader reissue(HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public List<String> getCurrentLoggedInUsers() {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
