package page.clab.api.service;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.repository.LoginAttemptLogRepository;
import page.clab.api.type.dto.GeoIpInfo;
import page.clab.api.type.dto.LoginAttemptLogResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.LoginAttemptResult;
import page.clab.api.util.GeoIpUtil;
import page.clab.api.util.HttpReqResUtil;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    private final MemberService memberService;

    public void createLoginAttemptLog(HttpServletRequest httpServletRequest, String memberId, LoginAttemptResult loginAttemptResult) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        GeoIpInfo geoIpInfo = GeoIpUtil.getInfoByIp(clientIpAddress);
        LoginAttemptLog loginAttemptLog = LoginAttemptLog.builder()
                .member(memberService.getMemberByIdOrThrow(memberId))
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(clientIpAddress)
                .location(geoIpInfo.getLocation())
                .loginAttemptResult(loginAttemptResult)
                .loginAttemptTime(LocalDateTime.now())
                .build();
        loginAttemptLogRepository.save(loginAttemptLog);
    }

    public PagedResponseDto<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId, Pageable pageable) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Page<LoginAttemptLog> loginAttemptLogs = getLoginAttemptByMember(pageable, member);
        return new PagedResponseDto<>(loginAttemptLogs.map(LoginAttemptLogResponseDto::of));
    }

    private Page<LoginAttemptLog> getLoginAttemptByMember(Pageable pageable, Member member) {
        return loginAttemptLogRepository.findAllByMemberOrderByLoginAttemptTimeDesc(member, pageable);
    }

}
