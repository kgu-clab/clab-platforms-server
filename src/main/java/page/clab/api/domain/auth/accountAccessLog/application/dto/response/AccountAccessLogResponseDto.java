package page.clab.api.domain.auth.accountAccessLog.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessLog;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessResult;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccountAccessLogResponseDto {

    private Long id;
    private String userAgent;
    private String ipAddress;
    private String location;
    private AccountAccessResult accountAccessResult;
    private LocalDateTime accessTime;

    public static AccountAccessLogResponseDto toDto(AccountAccessLog accountAccessLog) {
        return AccountAccessLogResponseDto.builder()
                .id(accountAccessLog.getId())
                .userAgent(accountAccessLog.getUserAgent())
                .ipAddress(accountAccessLog.getIpAddress())
                .location(accountAccessLog.getLocation())
                .accountAccessResult(accountAccessLog.getAccountAccessResult())
                .accessTime(accountAccessLog.getAccessTime())
                .build();
    }
}
