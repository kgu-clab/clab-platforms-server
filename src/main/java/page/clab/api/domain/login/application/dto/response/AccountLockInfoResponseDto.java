package page.clab.api.domain.login.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Getter
@Builder
public class AccountLockInfoResponseDto {

    private String id;

    private String name;

    public static AccountLockInfoResponseDto toDto(AccountLockInfo accountLockInfo, String memberName) {
        return AccountLockInfoResponseDto.builder()
                .id(accountLockInfo.getMemberId())
                .name(memberName)
                .build();
    }
}
