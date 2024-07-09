package page.clab.api.domain.login.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.application.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveLoginAttemptLogsUseCase {
    PagedResponseDto<LoginAttemptLogResponseDto> retrieveLoginAttemptLogs(String memberId, Pageable pageable);
}
