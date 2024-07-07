package page.clab.api.domain.login.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.dto.response.AccountLockInfoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBannedMembersUseCase {
    PagedResponseDto<AccountLockInfoResponseDto> retrieveBanMembers(Pageable pageable);
}
