package page.clab.api.domain.login.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Getter
@Builder
public class AccountLockInfoResponseDto {

    private String id;

    private String name;

    public static AccountLockInfoResponseDto toDto(AccountLockInfo accountLockInfo) {
        return AccountLockInfoResponseDto.builder()
                .id(accountLockInfo.getMember().getId())
                .name(accountLockInfo.getMember().getName())
                .build();
    }

}
