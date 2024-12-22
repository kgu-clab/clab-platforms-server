package page.clab.api.domain.auth.accountAccessLog.domain;

import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAccessLog {

    private Long id;
    private String memberId;
    private String userAgent;
    private String ipAddress;
    private String location;
    private AccountAccessResult accountAccessResult;
    private LocalDateTime accessTime;

    public static AccountAccessLog create(String memberId, HttpServletRequest httpServletRequest, String ipAddress,
        IPResponse ipResponse, AccountAccessResult accountAccessResult) {
        return AccountAccessLog.builder()
            .memberId(memberId)
            .userAgent(httpServletRequest.getHeader("User-Agent"))
            .ipAddress(ipAddress)
            .location(ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity())
            .accountAccessResult(accountAccessResult)
            .accessTime(LocalDateTime.now())
            .build();
    }
}
