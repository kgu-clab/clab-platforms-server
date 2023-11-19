package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.LoginAttemptLogRepository;
import page.clab.api.type.dto.GeoIpInfo;
import page.clab.api.type.dto.LoginAttemptLogResponseDto;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.LoginAttemptResult;
import page.clab.api.util.GeoIpUtil;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    private final MemberService memberService;

    public void createLoginAttemptLog(HttpServletRequest httpServletRequest, String memberId, LoginAttemptResult loginAttemptResult) {
        GeoIpInfo geoIpInfo = GeoIpUtil.getInfoByIp(httpServletRequest.getRemoteAddr());
        LoginAttemptLog loginAttemptLog = LoginAttemptLog.builder()
                .member(memberService.getMemberByIdOrThrow(memberId))
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .ipAddress(httpServletRequest.getRemoteAddr())
                .location(geoIpInfo.getLocation())
                .loginAttemptResult(loginAttemptResult)
                .loginAttemptTime(LocalDateTime.now())
                .build();
        loginAttemptLogRepository.save(loginAttemptLog);
    }

    public List<LoginAttemptLogResponseDto> getLoginAttemptLogs(String memberId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(currentMember)) {
            throw new PermissionDeniedException("관리자만 조회할 수 있습니다.");
        }
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Page<LoginAttemptLog> loginAttemptLogs = getLoginAttemptByMember(pageable, member);
        return loginAttemptLogs.map(LoginAttemptLogResponseDto::of).getContent();
    }

    private Page<LoginAttemptLog> getLoginAttemptByMember(Pageable pageable, Member member) {
        return loginAttemptLogRepository.findAllByMemberOrderByLoginAttemptTimeDesc(member, pageable);
    }

}
