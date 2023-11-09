package page.clab.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import page.clab.api.auth.exception.TokenValidateException;
import page.clab.api.auth.jwt.JwtTokenProvider;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.type.dto.MemberLoginRequestDto;
import page.clab.api.type.dto.RefreshTokenDto;
import page.clab.api.type.dto.TokenDto;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.LoginAttemptResult;
import page.clab.api.type.etc.Role;

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

    @Transactional
    public TokenInfo login(HttpServletRequest httpServletRequest, MemberLoginRequestDto memberLoginRequestDto) throws LoginFaliedException, MemberLockedException {
        String id = memberLoginRequestDto.getId();
        String password = memberLoginRequestDto.getPassword();
        Member member = memberService.getMemberByIdOrThrow(id);
        boolean loginSuccess = barunLogin(id, password);
        TokenInfo tokenInfo = null;
        if (loginSuccess) {
            loginFailInfoService.handleLoginFailInfo(id);
            tokenInfo = jwtTokenProvider.generateToken(id, member.getRole());
            memberService.setLastLoginTime(id);
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.SUCCESS);
        } else {
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.FAILURE);
            loginFailInfoService.updateLoginFailInfo(id);
        }
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

    public TokenInfo reissue(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            return tokenInfo;
        }
        return null;
    }

    public boolean checkTokenRole(TokenDto tokenDto) throws TokenValidateException {
        String token = tokenDto.getToken();
        if (tokenDto != null && jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.parseClaims(token);
            if (claims.get("role").toString().equals(Role.ADMIN.getKey())) {
                return true;
            }
        }
        else {
            throw new TokenValidateException();
        }
        return false;
    }

}
