package page.clab.api.domain.member.application;

import java.util.List;

public interface MembersByRecruitmentRegisterService {
    List<String> register(Long recruitmentId);
    String register(Long recruitmentId, String memberId);
}