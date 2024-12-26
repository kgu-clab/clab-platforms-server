package page.clab.api.domain.auth.accountLockInfo.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.accountLockInfo.application.dto.response.AccountLockInfoResponseDto;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

@Component
public class AccountLockInfoDtoMapper {

    public AccountLockInfoResponseDto toDto(AccountLockInfo accountLockInfo, String memberName) {
        return AccountLockInfoResponseDto.builder()
            .id(accountLockInfo.getMemberId())
            .name(memberName)
            .build();
    }
}
