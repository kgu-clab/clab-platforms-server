package page.clab.api.domain.login.application;

import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.dao.LoginAttemptLogRepository;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.IPInfoUtil;

@Service
@RequiredArgsConstructor
public class CreateLoginAttemptLogServiceImpl implements CreateLoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    @Transactional
    @Override
    public void execute(HttpServletRequest request, String memberId, LoginAttemptResult loginAttemptResult) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        IPResponse ipResponse = HttpReqResUtil.isBogonRequest(clientIpAddress) ? null : IPInfoUtil.getIpInfo(request);
        LoginAttemptLog loginAttemptLog = LoginAttemptLog.create(memberId, request, clientIpAddress, ipResponse, loginAttemptResult);
        loginAttemptLogRepository.save(loginAttemptLog);
    }
}