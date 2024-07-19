package page.clab.api.domain.auth.accountAccessLog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.accountAccessLog.application.dto.response.AccountAccessLogResponseDto;
import page.clab.api.domain.auth.accountAccessLog.application.port.in.RetrieveAccountAccessLogsUseCase;
import page.clab.api.domain.auth.accountAccessLog.application.port.out.RetrieveAccountAccessLogPort;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessLog;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class AccountAccessLogRetrievalService implements RetrieveAccountAccessLogsUseCase {

    private final RetrieveAccountAccessLogPort retrieveAccountAccessLogPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccountAccessLogResponseDto> retrieveAccountAccessLogs(String memberId, Pageable pageable) {
        Page<AccountAccessLog> accountAccessLogs = retrieveAccountAccessLogPort.findAllByMemberId(memberId, pageable);
        return new PagedResponseDto<>(accountAccessLogs.map(AccountAccessLogResponseDto::toDto));
    }
}
