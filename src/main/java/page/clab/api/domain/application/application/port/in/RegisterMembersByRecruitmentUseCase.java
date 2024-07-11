package page.clab.api.domain.application.application.port.in;

import java.util.List;

public interface RegisterMembersByRecruitmentUseCase {
    List<String> registerMembersByRecruitment(Long recruitmentId);

    String registerMembersByRecruitment(Long recruitmentId, String studentId);
}
