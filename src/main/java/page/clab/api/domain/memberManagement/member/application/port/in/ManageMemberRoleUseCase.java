package page.clab.api.domain.memberManagement.member.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.memberManagement.member.application.dto.request.ChangeMemberRoleRequest;

public interface ManageMemberRoleUseCase {
    String changeMemberRole(HttpServletRequest httpServletRequest, String memberId, ChangeMemberRoleRequest request);
}
