package page.clab.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import page.clab.api.auth.jwt.JwtTokenProvider;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.type.dto.LoginRequestDto;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.dto.TwoFactorAuthenticationRequestDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.RedisToken;
import page.clab.api.type.etc.LoginAttemptResult;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;

    private final LoginFailInfoService loginFailInfoService;

    private final MemberService memberService;

    private final LoginAttemptLogService loginAttemptLogService;

    private final RedisTokenService redisTokenService;

    private final AuthenticatorService authenticatorService;

    @Transactional
    public String login(HttpServletRequest httpServletRequest, LoginRequestDto loginRequestDto) throws LoginFaliedException, MemberLockedException {
        String id = loginRequestDto.getId();
        String password = loginRequestDto.getPassword();
        Member member = memberService.getMemberById(id);
        if (member == null) {
            throw new LoginFaliedException("존재하지 않는 아이디입니다.");
        }
        boolean loginSuccess = barunLogin(id, password);
        if (loginSuccess) {
            loginFailInfoService.handleLoginFailInfo(id);
            memberService.setLastLoginTime(id);
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.TOTP);
            if (!authenticatorService.isAuthenticatorExist(id)) {
                authenticatorService.generateSecretKey(id);
            }
            return authenticatorService.getAuthenticatorById(id).getSecretKey();
        } else {
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.FAILURE);
            loginFailInfoService.updateLoginFailInfo(id);
        }
        return null;
    }

    public TokenInfo authenticator(HttpServletRequest httpServletRequest, TwoFactorAuthenticationRequestDto twoFactorAuthenticationRequestDto) throws LoginFaliedException {
        String id = twoFactorAuthenticationRequestDto.getMemberId();
        String totp = twoFactorAuthenticationRequestDto.getTotp();
        if (!authenticatorService.isAuthenticatorValid(id, totp)) {
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.FAILURE);
            throw new LoginFaliedException("잘못된 인증번호입니다.");
        }
        loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.SUCCESS);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(id, memberService.getMemberById(id).getRole());
        redisTokenService.saveRedisToken(id, memberService.getMemberById(id).getRole(), tokenInfo, httpServletRequest.getRemoteAddr());
        return tokenInfo;
    }

    private boolean barunLogin(String id, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", id);
        formData.add("password", password);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://barun.kyonggi.ac.kr")
                .build();

        String response = webClient
                .post()
                .uri("/ko/process/member/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(response, Map.class);
            return (boolean) jsonResponse.get("success");
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public TokenInfo reissue(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        RedisToken redisToken = redisTokenService.getRedisTokenByRefreshToken(token);
        if (!redisToken.getIp().equals(request.getRemoteAddr())) {
            redisTokenService.deleteRedisTokenByAccessToken(redisToken.getAccessToken());
            throw new SecurityException("올바르지 않은 토큰 재발급 시도가 감지되어 토큰을 삭제하였습니다.");
        }
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(redisToken.getId(), redisToken.getRole());
        redisTokenService.saveRedisToken(redisToken.getId(), redisToken.getRole(), tokenInfo, redisToken.getIp());
        return tokenInfo;
    }

}
