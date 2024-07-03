package page.clab.api.domain.member.application.port.in;

import java.util.List;

public interface MembersByRecruitmentRegisterUseCase {
    List<String> register(Long recruitmentId);
    String register(Long recruitmentId, String memberId);
}