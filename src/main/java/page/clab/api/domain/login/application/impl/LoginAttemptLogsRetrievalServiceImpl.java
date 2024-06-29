package page.clab.api.domain.login.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.LoginAttemptLogsRetrievalService;
import page.clab.api.domain.login.dao.LoginAttemptLogRepository;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogsRetrievalServiceImpl implements LoginAttemptLogsRetrievalService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<LoginAttemptLogResponseDto> retrieve(String memberId, Pageable pageable) {
        Page<LoginAttemptLog> loginAttemptLogs = loginAttemptLogRepository.findAllByMemberId(memberId, pageable);
        return new PagedResponseDto<>(loginAttemptLogs.map(LoginAttemptLogResponseDto::toDto));
    }
}