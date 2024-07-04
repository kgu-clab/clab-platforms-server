package page.clab.api.domain.member.application.port.in;

import java.util.List;

public interface RegisterMembersByRecruitmentUseCase {
    List<String> registerMembersByRecruitment(Long recruitmentId);
    String registerMembersByRecruitment(Long recruitmentId, String memberId);
}
