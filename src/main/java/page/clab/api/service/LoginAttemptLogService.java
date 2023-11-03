package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.repository.LoginAttemptLogRepository;
import page.clab.api.type.dto.LoginAttemptLogResponseDto;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginAttemptLogService {

    private final LoginAttemptLogRepository loginAttemptLogRepository;

    private final MemberService memberService;

    public void createLoginAttemptLog(LoginAttemptLog loginAttemptLog) {
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
