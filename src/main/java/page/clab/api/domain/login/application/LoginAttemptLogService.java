package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.dao.LoginAttemptLogRepository;
import page.clab.api.domain.login.domain.GeoIpInfo;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.GeoIpUtil;
import page.clab.api.global.util.HttpReqResUtil;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    public void createLoginAttemptLog(HttpServletRequest httpServletRequest, String memberId, LoginAttemptResult loginAttemptResult) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        GeoIpInfo geoIpInfo = GeoIpUtil.getInfoByIp(clientIpAddress);
        LoginAttemptLog loginAttemptLog = LoginAttemptLog.builder()
                .memberId(memberId)
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(clientIpAddress)
                .location(geoIpInfo.getLocation())
                .loginAttemptResult(loginAttemptResult)
                .loginAttemptTime(LocalDateTime.now())
                .build();
        loginAttemptLogRepository.save(loginAttemptLog);
    }

    public PagedResponseDto<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId, Pageable pageable) {
        Page<LoginAttemptLog> loginAttemptLogs = getLoginAttemptByMemberId(pageable, memberId);
        return new PagedResponseDto<>(loginAttemptLogs.map(LoginAttemptLogResponseDto::of));
    }

    private Page<LoginAttemptLog> getLoginAttemptByMemberId(Pageable pageable, String memberId) {
        return loginAttemptLogRepository.findAllByMemberIdOrderByLoginAttemptTimeDesc(memberId, pageable);
    }

}
