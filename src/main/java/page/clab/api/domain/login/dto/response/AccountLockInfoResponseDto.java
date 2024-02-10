package page.clab.api.domain.login.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountLockInfoResponseDto {

    private String id;

    private String name;

    public static AccountLockInfoResponseDto of(AccountLockInfo accountLockInfo) {
        return AccountLockInfoResponseDto.builder()
                .id(accountLockInfo.getMember().getId())
                .name(accountLockInfo.getMember().getName())
                .build();
    }

}
