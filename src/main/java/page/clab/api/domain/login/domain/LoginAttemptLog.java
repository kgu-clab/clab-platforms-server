package page.clab.api.domain.login.domain;

import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginAttemptLog {

    private Long id;
    private String memberId;
    private String userAgent;
    private String ipAddress;
    private String location;
    private LoginAttemptResult loginAttemptResult;
    private LocalDateTime loginAttemptTime;

    public static LoginAttemptLog create(String memberId, HttpServletRequest httpServletRequest, String ipAddress, IPResponse ipResponse, LoginAttemptResult loginAttemptResult) {
        return LoginAttemptLog.builder()
                .memberId(memberId)
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(ipAddress)
                .location(ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity())
                .loginAttemptResult(loginAttemptResult)
                .loginAttemptTime(LocalDateTime.now())
                .build();
    }
}
