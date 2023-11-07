package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.repository.LoginAttemptLogRepository;
import page.clab.api.type.dto.GeoIpInfo;
import page.clab.api.type.dto.LoginAttemptLogResponseDto;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.LoginAttemptResult;
import page.clab.api.util.GeoIpUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    private final MemberService memberService;

    public void createLoginAttemptLog(HttpServletRequest httpServletRequest, String memberId, LoginAttemptResult loginAttemptResult) {
//        GeoIpInfo geoIpInfo = GeoIpUtil.getInfoByIp(httpServletRequest.getRemoteAddr());
        LoginAttemptLog loginAttemptLog = LoginAttemptLog.builder()
                .member(memberService.getMemberByIdOrThrow(memberId))
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(httpServletRequest.getRemoteAddr())
//                .location(geoIpInfo.getCountry() + geoIpInfo.getCity())
                .loginAttemptResult(loginAttemptResult)
                .loginAttemptTime(LocalDateTime.now())
                .build();
        loginAttemptLogRepository.save(loginAttemptLog);
    }

    public List<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        List<LoginAttemptLog> loginAttemptLogs = loginAttemptLogRepository.findAllByMember(member);
        return loginAttemptLogs.stream()
                .map(LoginAttemptLogResponseDto::of)
                .collect(Collectors.toList());
    }

}
