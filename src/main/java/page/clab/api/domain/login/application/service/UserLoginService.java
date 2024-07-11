package page.clab.api.domain.login.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.application.dto.response.LoginHeader;
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
import page.clab.api.domain.memberManagement.member.application.port.in.UpdateMemberUseCase;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Qualifier("userLoginService")
public class UserLoginService implements ManageLoginUseCase {

    @Qualifier("loginAuthenticationManager")
    private final AuthenticationManager loginAuthenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final ManageAccountLockUseCase manageAccountLockUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final UpdateMemberUseCase updateMemberUseCase;
    private final ManageLoginAttemptLogUseCase manageLoginAttemptLogUseCase;
    private final ManageRedisTokenUseCase manageRedisTokenUseCase;
    private final ManageAuthenticatorUseCase manageAuthenticatorUseCase;

    @Transactional
    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFailedException, MemberLockedException {
        authenticateAndCheckStatus(request, requestDto);
        logLoginAttempt(request, requestDto.getId(), true);
        MemberLoginInfoDto loginMember = retrieveMemberInfoUseCase.getMemberLoginInfoById(requestDto.getId());
        updateMemberUseCase.updateLastLoginTime(requestDto.getId());
        return generateLoginResult(loginMember);
    }

    private void authenticateAndCheckStatus(HttpServletRequest httpServletRequest, LoginRequestDto loginRequestDto) throws LoginFailedException, MemberLockedException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPassword());
            loginAuthenticationManager.authenticate(authenticationToken);
            manageAccountLockUseCase.handleAccountLockInfo(loginRequestDto.getId());
        } catch (BadCredentialsException e) {
            logLoginAttempt(httpServletRequest, loginRequestDto.getId(), false);
            manageAccountLockUseCase.handleLoginFailure(httpServletRequest, loginRequestDto.getId());
            throw new LoginFailedException();
        }
    }

    private void logLoginAttempt(HttpServletRequest request, String memberId, boolean isSuccess) {
        LoginAttemptResult result = isSuccess ? LoginAttemptResult.SUCCESS : LoginAttemptResult.FAILURE;
        manageLoginAttemptLogUseCase.logLoginAttempt(request, memberId, result);
    }

    private LoginResult generateLoginResult(MemberLoginInfoDto loginMember) {
        String memberId = loginMember.getMemberId();
        String header;
        boolean isOtpEnabled = Optional.of(loginMember.isOtpEnabled()).orElse(false);
        if (isOtpEnabled || loginMember.isAdminRole()) {
            if (!manageAuthenticatorUseCase.isAuthenticatorExist(memberId)) {
                String secretKey = manageAuthenticatorUseCase.generateSecretKey(memberId);
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
        manageRedisTokenUseCase.saveToken(memberInfo.getMemberId(), memberInfo.getRole(), tokenInfo, clientIpAddress);
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