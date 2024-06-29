package page.clab.api.domain.login.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.FetchLoginAttemptLogsService;
import page.clab.api.domain.login.dao.LoginAttemptLogRepository;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchLoginAttemptLogsServiceImpl implements FetchLoginAttemptLogsService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId, Pageable pageable) {
        Page<LoginAttemptLog> loginAttemptLogs = loginAttemptLogRepository.findAllByMemberId(memberId, pageable);
        return new PagedResponseDto<>(loginAttemptLogs.map(LoginAttemptLogResponseDto::toDto));
    }
}