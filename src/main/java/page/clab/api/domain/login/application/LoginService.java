package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginHeader;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.auth.exception.TokenForgeryException;
import page.clab.api.global.auth.exception.TokenMisuseException;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    @Qualifier("loginAuthenticationManager")
    private final AuthenticationManager loginAuthenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final AccountLockInfoService accountLockInfoService;

    private final MemberService memberService;

    private final LoginAttemptLogService loginAttemptLogService;

    private final RedisTokenService redisTokenService;

    private final AuthenticatorService authenticatorService;

    private final SlackService slackService;

    @Transactional
    public String login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFaliedException, MemberLockedException {
        authenticateAndCheckStatus(request, requestDto);
        logLoginAttempt(request, requestDto.getId(), true);
        Member loginMember = memberService.getMemberByIdOrThrow(requestDto.getId());
        loginMember.updateLastLoginTime();
        return generateLoginHeader(loginMember);
    }

    @Transactional
    public TokenHeader authenticator(HttpServletRequest request, TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto) throws LoginFaliedException, MemberLockedException {
        String memberId = twoFactorAuthenticationRequestDto.getMemberId();
        Member loginMember = memberService.getMemberById(memberId);
        String totp = twoFactorAuthenticationRequestDto.getTotp();

        accountLockInfoService.handleAccountLockInfo(memberId);
        verifyTwoFactorAuthentication(memberId, totp, request);

        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        sendAdminLoginNotification(request, loginMember);
        return TokenHeader.create(tokenInfo);
    }

    public String resetAuthenticator(String memberId) {
        return authenticatorService.resetAuthenticator(memberId);
    }

    public String revoke(String memberId) {
        Member member = memberService.getMemberById(memberId);
        redisTokenService.deleteRedisTokenByMemberId(memberId);
        return member.getId();
    }

    @Transactional
    public TokenHeader reissue(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        RedisToken redisToken = redisTokenService.getRedisTokenByRefreshToken(refreshToken);

        validateMemberExistence(authentication);
        validateToken(redisToken);

        TokenInfo newTokenInfo = jwtTokenProvider.generateToken(redisToken.getId(), redisToken.getRole());
        redisTokenService.saveRedisToken(redisToken.getId(), redisToken.getRole(), newTokenInfo, redisToken.getIp());
        return TokenHeader.create(newTokenInfo);
    }

    public List<String> getCurrentLoggedInUsers() {
        return redisTokenService.getCurrentLoggedInUsers();
    }

    private void authenticateAndCheckStatus(HttpServletRequest httpServletRequest, LoginRequestDto loginRequestDto) throws LoginFaliedException, MemberLockedException {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getId(), loginRequestDto.getPassword());
            loginAuthenticationManager.authenticate(authenticationToken);
            accountLockInfoService.handleAccountLockInfo(loginRequestDto.getId());
        } catch (BadCredentialsException e) {
            logLoginAttempt(httpServletRequest, loginRequestDto.getId(), false);
            accountLockInfoService.handleLoginFailure(httpServletRequest, loginRequestDto.getId());
            throw new LoginFaliedException();
        }
    }

    private void logLoginAttempt(HttpServletRequest request, String memberId, boolean isSuccess) {
        LoginAttemptResult result = isSuccess ? LoginAttemptResult.SUCCESS : LoginAttemptResult.FAILURE;
        loginAttemptLogService.createLoginAttemptLog(request, memberId, result);
    }

    private String generateLoginHeader(Member loginMember) {
        String memberId = loginMember.getId();
        if (loginMember.getIsOtpEnabled()) {
            if (!authenticatorService.isAuthenticatorExist(memberId)) {
                String secretKey = authenticatorService.generateSecretKey(memberId);
                return LoginHeader.create(secretKey).toJson();
            }
            return TokenHeader.create().toJson();
        }
        TokenInfo tokenInfo = generateAndSaveToken(loginMember);
        return TokenHeader.create(tokenInfo).toJson();
    }

    private void verifyTwoFactorAuthentication(String memberId, String totp, HttpServletRequest request) throws MemberLockedException, LoginFaliedException {
        if (!authenticatorService.isAuthenticatorValid(memberId, totp)) {
            loginAttemptLogService.createLoginAttemptLog(request, memberId, LoginAttemptResult.FAILURE);
            accountLockInfoService.handleLoginFailure(request, memberId);
            throw new LoginFaliedException("잘못된 인증번호입니다.");
        }
        loginAttemptLogService.createLoginAttemptLog(request, memberId, LoginAttemptResult.TOTP);
    }

    private TokenInfo generateAndSaveToken(Member member) {
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId(), member.getRole());
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        redisTokenService.saveRedisToken(member.getId(), member.getRole(), tokenInfo, clientIpAddress);
        return tokenInfo;
    }

    private void sendAdminLoginNotification(HttpServletRequest request, Member loginMember) {
        if (loginMember.isSuperAdminRole()) {
            slackService.sendAdminLoginNotification(request, loginMember);
        }
    }

    private void validateMemberExistence(Authentication authentication) {
        String id = authentication.getName();
        Member member = memberService.getMemberById(id);
        if (member == null) {
            throw new TokenForgeryException("존재하지 않는 회원에 대한 토큰입니다.");
        }
    }

    private void validateToken(RedisToken redisToken) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (!redisToken.isSameIp(clientIpAddress)) {
            redisTokenService.deleteRedisTokenByAccessToken(redisToken.getAccessToken());
            throw new TokenMisuseException("[" + clientIpAddress + "] 토큰 발급 IP와 다른 IP에서 발급을 시도하여 토큰을 삭제하였습니다.");
        }
    }

}
