package page.clab.api.domain.auth.login.application.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessResult;
import page.clab.api.domain.auth.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.auth.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.auth.login.application.dto.response.LoginHeader;
import page.clab.api.domain.auth.login.application.dto.response.LoginResult;
import page.clab.api.domain.auth.login.application.dto.response.TokenHeader;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.application.port.in.ManageAuthenticatorUseCase;
import page.clab.api.domain.auth.login.application.port.in.ManageLoginUseCase;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.external.auth.accountAccessLog.application.port.ExternalRegisterAccountAccessLogUseCase;
import page.clab.api.external.auth.accountLockInfo.application.ExternalManageAccountLockUseCase;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalUpdateMemberUseCase;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;
import page.clab.api.global.util.HttpReqResUtil;

@Service
@RequiredArgsConstructor
@Qualifier("userLoginService")
public class UserLoginService implements ManageLoginUseCase {

    private final ManageAuthenticatorUseCase manageAuthenticatorUseCase;
    private final ExternalManageAccountLockUseCase externalManageAccountLockUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalUpdateMemberUseCase externalUpdateMemberUseCase;
    private final ExternalRegisterAccountAccessLogUseCase registerAccountAccessLogUseCase;
    private final ExternalManageRedisTokenUseCase manageRedisTokenUseCase;

    @Qualifier("loginAuthenticationManager")
    private final AuthenticationManager loginAuthenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) {
        authenticateAndCheckStatus(request, requestDto);
        registerAccountAccessLog(request, requestDto.getId(), true);
        MemberLoginInfoDto loginMember = externalRetrieveMemberUseCase.getMemberLoginInfoById(requestDto.getId());
        externalUpdateMemberUseCase.updateLastLoginTime(requestDto.getId());
        return generateLoginResult(loginMember);
    }

    @Transactional
    @Override
    public LoginResult guestLogin(HttpServletRequest request) {
        MemberLoginInfoDto guestMember = externalRetrieveMemberUseCase.getGuestMemberLoginInfo();
        registerAccountAccessLogUseCase.registerAccountAccessLog(request, guestMember.getMemberId(),
            AccountAccessResult.SUCCESS);
        externalUpdateMemberUseCase.updateLastLoginTime(guestMember.getMemberId());
        return generateLoginResult(guestMember);
    }

    private void authenticateAndCheckStatus(HttpServletRequest httpServletRequest, LoginRequestDto loginRequestDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPassword());
            loginAuthenticationManager.authenticate(authenticationToken);
            externalManageAccountLockUseCase.handleAccountLockInfo(loginRequestDto.getId());
        } catch (BadCredentialsException e) {
            registerAccountAccessLog(httpServletRequest, loginRequestDto.getId(), false);
            externalManageAccountLockUseCase.handleLoginFailure(httpServletRequest, loginRequestDto.getId());
            throw new BaseException(ErrorCode.BAD_CREDENTIALS, "잘못된 아이디 또는 비밀번호입니다.");
        }
    }

    private void registerAccountAccessLog(HttpServletRequest request, String memberId, boolean isSuccess) {
        AccountAccessResult result = isSuccess ? AccountAccessResult.SUCCESS : AccountAccessResult.FAILURE;
        registerAccountAccessLogUseCase.registerAccountAccessLog(request, memberId, result);
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
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public String resetAuthenticator(String memberId) {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public TokenHeader reissueToken(HttpServletRequest request) {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public List<String> retrieveCurrentLoggedInUsers() {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }
}
