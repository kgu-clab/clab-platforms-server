package page.clab.api.domain.member.application;

import java.util.List;

public interface CreateMembersByRecruitmentService {
    List<String> createMembersByRecruitmentId(Long recruitmentId);
    String createMemberByRecruitmentId(Long recruitmentId, String memberId);
}