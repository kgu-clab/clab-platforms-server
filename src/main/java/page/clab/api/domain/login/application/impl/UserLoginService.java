package page.clab.api.domain.login.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.AccountLockManagementService;
import page.clab.api.domain.login.application.AuthenticatorService;
import page.clab.api.domain.login.application.LoginAttemptLogManagementService;
import page.clab.api.domain.login.application.LoginService;
import page.clab.api.domain.login.application.RedisTokenManagementService;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginHeader;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Qualifier("userLoginService")
public class UserLoginService implements LoginService {

    @Qualifier("loginAuthenticationManager")
    private final AuthenticationManager loginAuthenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final AccountLockManagementService accountLockManagementService;

    private final MemberLookupService memberLookupService;

    private final LoginAttemptLogManagementService loginAttemptLogManagementService;

    private final RedisTokenManagementService redisTokenManagementService;

    private final AuthenticatorService authenticatorService;

    @Transactional
    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFaliedException, MemberLockedException {
        authenticateAndCheckStatus(request, requestDto);
        logLoginAttempt(request, requestDto.getId(), true);
        MemberLoginInfoDto loginMember = memberLookupService.getMemberLoginInfoById(requestDto.getId());
        memberLookupService.updateLastLoginTime(requestDto.getId());
        return generateLoginResult(loginMember);
    }

    private void authenticateAndCheckStatus(HttpServletRequest httpServletRequest, LoginRequestDto loginRequestDto) throws LoginFaliedException, MemberLockedException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPassword());
            loginAuthenticationManager.authenticate(authenticationToken);
            accountLockManagementService.handleAccountLockInfo(loginRequestDto.getId());
        } catch (BadCredentialsException e) {
            logLoginAttempt(httpServletRequest, loginRequestDto.getId(), false);
            accountLockManagementService.handleLoginFailure(httpServletRequest, loginRequestDto.getId());
            throw new LoginFaliedException();
        }
    }

    private void logLoginAttempt(HttpServletRequest request, String memberId, boolean isSuccess) {
        LoginAttemptResult result = isSuccess ? LoginAttemptResult.SUCCESS : LoginAttemptResult.FAILURE;
        loginAttemptLogManagementService.logLoginAttempt(request, memberId, result);
    }

    private LoginResult generateLoginResult(MemberLoginInfoDto loginMember) {
        String memberId = loginMember.getMemberId();
        String header;
        boolean isOtpEnabled = Optional.of(loginMember.isOtpEnabled()).orElse(false);
        if (isOtpEnabled || loginMember.isAdminRole()) {
            if (!authenticatorService.isAuthenticatorExist(memberId)) {
                String secretKey = authenticatorService.generateSecretKey(memberId);
                header = LoginHeader.create(secretKey).toJson();
                return LoginResult.create(header, true);
            }
            header = TokenHeader.create().toJson();
            return LoginResult.create(header, true);
        }
        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        header = TokenHeader.create(tokenInfo).toJson();
        return LoginResult.create(header, false);
    }

    private TokenInfo generateAndSaveToken(MemberLoginInfoDto memberInfo) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(memberInfo.getMemberId(), memberInfo.getRole());
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        redisTokenManagementService.saveToken(memberInfo.getMemberId(), memberInfo.getRole(), tokenInfo, clientIpAddress);
        return tokenInfo;
    }

    @Override
    public LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto requestDto) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String resetAuthenticator(String memberId) {
        throw new UnsupportedOperationException("Method not implemented");
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
