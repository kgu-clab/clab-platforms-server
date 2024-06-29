package page.clab.api.domain.login.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchLoginAttemptLogsService {
    PagedResponseDto<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId, Pageable pageable);
}