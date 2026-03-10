package page.clab.api.domain.auth.login.application.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessResult;
import page.clab.api.domain.auth.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.auth.login.application.dto.request.TwoFactorAuthenticationRequestDto;
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
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.GeneralAlertType;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;
import page.clab.api.global.util.HttpReqResUtil;

@Service
@RequiredArgsConstructor
@Qualifier("twoFactorAuthenticationService")
public class TwoFactorAuthenticationService implements ManageLoginUseCase {

    private final ManageAuthenticatorUseCase manageAuthenticatorUseCase;
    private final ExternalManageAccountLockUseCase externalManageAccountLockUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRegisterAccountAccessLogUseCase externalRegisterAccountAccessLogUseCase;
    private final ExternalManageRedisTokenUseCase externalManageRedisTokenUseCase;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public LoginResult authenticate(HttpServletRequest request,
        TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto
    ) {
        String memberId = twoFactorAuthenticationRequestDto.getMemberId();
        MemberLoginInfoDto loginMember = externalRetrieveMemberUseCase.getMemberLoginInfoById(memberId);
        String totp = twoFactorAuthenticationRequestDto.getTotp();

        externalManageAccountLockUseCase.handleAccountLockInfo(memberId);
        verifyTwoFactorAuthentication(memberId, totp, request);

        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        sendAdminLoginNotification(request, loginMember);
        String header = TokenHeader.create(tokenInfo).toJson();
        return LoginResult.create(header, true);
    }

    private void verifyTwoFactorAuthentication(String memberId, String totp, HttpServletRequest request) {
        if (!manageAuthenticatorUseCase.isAuthenticatorValid(memberId, totp)) {
            externalRegisterAccountAccessLogUseCase.registerAccountAccessLog(request, memberId,
                AccountAccessResult.FAILURE);
            externalManageAccountLockUseCase.handleLoginFailure(request, memberId);
            throw new BaseException(ErrorCode.BAD_CREDENTIALS, "잘못된 인증번호입니다.");
        }
        externalRegisterAccountAccessLogUseCase.registerAccountAccessLog(request, memberId, AccountAccessResult.TOTP);
    }

    private TokenInfo generateAndSaveToken(MemberLoginInfoDto memberInfo) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(memberInfo.getMemberId(), memberInfo.getRole());
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        externalManageRedisTokenUseCase.saveToken(memberInfo.getMemberId(), memberInfo.getRole(), tokenInfo,
            clientIpAddress);
        return tokenInfo;
    }

    private void sendAdminLoginNotification(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        if (loginMember.isSuperAdminRole()) {
            eventPublisher.publishEvent(
                new NotificationEvent(this, GeneralAlertType.ADMIN_LOGIN, request, loginMember));
        }
    }

    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public String resetAuthenticator(String memberId) {
        return manageAuthenticatorUseCase.resetAuthenticator(memberId);
    }

    @Override
    public TokenHeader reissueToken(HttpServletRequest request) {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public List<String> retrieveCurrentLoggedInUsers() {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    public LoginResult guestLogin(HttpServletRequest request) {
        throw new BaseException(ErrorCode.UNSUPPORTED_OPERATION);
    }
}
