package page.clab.api.domain.login.application;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.dao.LoginAttemptLogRepository;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.login.domain.GeoIpInfo;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.dto.PagedResponseDto;
import page.clab.api.domain.login.domain.LoginAttemptLog;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.global.util.GeoIpUtil;
import page.clab.api.global.util.HttpReqResUtil;

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
