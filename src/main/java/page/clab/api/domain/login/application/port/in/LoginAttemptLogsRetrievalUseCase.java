package page.clab.api.domain.login.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface LoginAttemptLogsRetrievalUseCase {
    PagedResponseDto<LoginAttemptLogResponseDto> retrieve(String memberId, Pageable pageable);
}