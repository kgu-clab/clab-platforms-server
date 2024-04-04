package page.clab.api.domain.login.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginAttemptLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    private String userAgent;

    private String ipAddress;

    private String location;

    @Enumerated(EnumType.STRING)
    private LoginAttemptResult loginAttemptResult;

    private LocalDateTime loginAttemptTime;

    public static LoginAttemptLog create(String memberId, HttpServletRequest httpServletRequest, String ipAddress, GeoIpInfo geoIpInfo, LoginAttemptResult loginAttemptResult) {
        return LoginAttemptLog.builder()
                .memberId(memberId)
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(ipAddress)
                .location(geoIpInfo.getLocation())
                .loginAttemptResult(loginAttemptResult)
                .loginAttemptTime(LocalDateTime.now())
                .build();
    }

}
