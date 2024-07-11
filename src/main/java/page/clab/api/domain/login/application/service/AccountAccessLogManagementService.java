package page.clab.api.domain.login.application.service;

import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.RegisterAccountAccessLogUseCase;
import page.clab.api.domain.login.application.port.out.RegisterAccountAccessLogPort;
import page.clab.api.domain.login.domain.AccountAccessLog;
import page.clab.api.domain.login.domain.AccountAccessResult;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.IPInfoUtil;

@Service
@RequiredArgsConstructor
public class AccountAccessLogManagementService implements RegisterAccountAccessLogUseCase {

    private final RegisterAccountAccessLogPort registerAccountAccessLogPort;

    @Transactional
    @Override
    public void registerAccountAccessLog(HttpServletRequest request, String memberId, AccountAccessResult accountAccessResult) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        IPResponse ipResponse = HttpReqResUtil.isBogonRequest(clientIpAddress) ? null : IPInfoUtil.getIpInfo(request);
        AccountAccessLog accountAccessLog = AccountAccessLog.create(memberId, request, clientIpAddress, ipResponse, accountAccessResult);
        registerAccountAccessLogPort.save(accountAccessLog);
    }
}
