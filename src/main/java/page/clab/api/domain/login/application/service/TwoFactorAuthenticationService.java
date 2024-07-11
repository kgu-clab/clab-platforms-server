package page.clab.api.domain.login.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.application.dto.response.LoginResult;
import page.clab.api.domain.login.application.dto.response.TokenHeader;
import page.clab.api.domain.login.application.dto.response.TokenInfo;
import page.clab.api.domain.login.application.exception.LoginFailedException;
import page.clab.api.domain.login.application.exception.MemberLockedException;
import page.clab.api.domain.login.application.port.in.ManageAccountLockUseCase;
import page.clab.api.domain.login.application.port.in.ManageAuthenticatorUseCase;
import page.clab.api.domain.login.application.port.in.ManageLoginAttemptLogUseCase;
import page.clab.api.domain.login.application.port.in.ManageLoginUseCase;
import page.clab.api.domain.login.application.port.in.ManageRedisTokenUseCase;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("twoFactorAuthenticationService")
public class TwoFactorAuthenticationService implements ManageLoginUseCase {

    private final ManageAccountLockUseCase manageAccountLockUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final ManageLoginAttemptLogUseCase manageLoginAttemptLogUseCase;
    private final ManageRedisTokenUseCase manageRedisTokenUseCase;
    private final ManageAuthenticatorUseCase manageAuthenticatorUseCase;
    private final SlackService slackService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto) throws LoginFailedException, MemberLockedException {
        String memberId = twoFactorAuthenticationRequestDto.getMemberId();
        MemberLoginInfoDto loginMember = retrieveMemberInfoUseCase.getMemberLoginInfoById(memberId);
        String totp = twoFactorAuthenticationRequestDto.getTotp();

        manageAccountLockUseCase.handleAccountLockInfo(memberId);
        verifyTwoFactorAuthentication(memberId, totp, request);

        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        sendAdminLoginNotification(request, loginMember);
        String header = TokenHeader.create(tokenInfo).toJson();
        return LoginResult.create(header, true);
    }

    private void verifyTwoFactorAuthentication(String memberId, String totp, HttpServletRequest request) throws MemberLockedException, LoginFailedException {
        if (!manageAuthenticatorUseCase.isAuthenticatorValid(memberId, totp)) {
            manageLoginAttemptLogUseCase.logLoginAttempt(request, memberId, LoginAttemptResult.FAILURE);
            manageAccountLockUseCase.handleLoginFailure(request, memberId);
            throw new LoginFailedException("잘못된 인증번호입니다.");
        }
        manageLoginAttemptLogUseCase.logLoginAttempt(request, memberId, LoginAttemptResult.TOTP);
    }

    private TokenInfo generateAndSaveToken(MemberLoginInfoDto memberInfo) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(memberInfo.getMemberId(), memberInfo.getRole());
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        manageRedisTokenUseCase.saveToken(memberInfo.getMemberId(), memberInfo.getRole(), tokenInfo, clientIpAddress);
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
        return manageAuthenticatorUseCase.resetAuthenticator(memberId);
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