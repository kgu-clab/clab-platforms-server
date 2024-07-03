package page.clab.api.domain.login.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.RetrieveLoginAttemptLogsUseCase;
import page.clab.api.domain.login.application.port.out.RetrieveLoginAttemptLogPort;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogsRetrievalService implements RetrieveLoginAttemptLogsUseCase {

    private final RetrieveLoginAttemptLogPort retrieveLoginAttemptLogPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<LoginAttemptLogResponseDto> retrieve(String memberId, Pageable pageable) {
        Page<LoginAttemptLog> loginAttemptLogs = retrieveLoginAttemptLogPort.findAllByMemberId(memberId, pageable);
        return new PagedResponseDto<>(loginAttemptLogs.map(LoginAttemptLogResponseDto::toDto));
    }
}
