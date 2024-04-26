package page.clab.api.domain.login.application;

import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.dao.LoginAttemptLogRepository;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.IPInfoUtil;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    @Transactional
    public void createLoginAttemptLog(HttpServletRequest request, String memberId, LoginAttemptResult loginAttemptResult) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        IPResponse ipResponse = IPInfoUtil.getIpInfo(request);
        LoginAttemptLog loginAttemptLog = LoginAttemptLog.create(memberId, request, clientIpAddress, ipResponse, loginAttemptResult);
        loginAttemptLogRepository.save(loginAttemptLog);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId, Pageable pageable) {
        Page<LoginAttemptLog> loginAttemptLogs = getLoginAttemptByMemberId(pageable, memberId);
        return new PagedResponseDto<>(loginAttemptLogs.map(LoginAttemptLogResponseDto::toDto));
    }

    private Page<LoginAttemptLog> getLoginAttemptByMemberId(Pageable pageable, String memberId) {
        return loginAttemptLogRepository.findAllByMemberIdOrderByLoginAttemptTimeDesc(memberId, pageable);
    }

}
