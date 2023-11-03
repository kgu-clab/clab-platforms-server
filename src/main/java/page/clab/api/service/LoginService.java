package page.clab.api.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import page.clab.api.auth.exception.TokenValidateException;
import page.clab.api.auth.jwt.JwtTokenProvider;
import page.clab.api.exception.LoginFaliedException;
import page.clab.api.exception.MemberLockedException;
import page.clab.api.type.dto.MemberLoginRequestDto;
import page.clab.api.type.dto.RefreshTokenDto;
import page.clab.api.type.dto.TokenDto;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.etc.LoginAttemptResult;
import page.clab.api.type.etc.Role;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    private final LoginFailInfoService loginFailInfoService;

    private final MemberService memberService;

    private final LoginAttemptLogService loginAttemptLogService;

    @Transactional
    public TokenInfo login(HttpServletRequest httpServletRequest, MemberLoginRequestDto memberLoginRequestDto) throws LoginFaliedException, MemberLockedException {
        String id = memberLoginRequestDto.getId();
        String password = memberLoginRequestDto.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            loginFailInfoService.handleLoginFailInfo(authentication.getName());
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            memberService.setLastLoginTime(authentication.getName());
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, authentication.getName(), LoginAttemptResult.SUCCESS);
            return tokenInfo;
        } catch (BadCredentialsException e) {
            loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.FAILURE);
            loginFailInfoService.updateLoginFailInfo(id);
        }
        loginAttemptLogService.createLoginAttemptLog(httpServletRequest, id, LoginAttemptResult.FAILURE);
        return null;
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
