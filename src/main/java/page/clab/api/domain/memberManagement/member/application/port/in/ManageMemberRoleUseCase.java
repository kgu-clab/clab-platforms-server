package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.ChangeMemberRoleRequest;

public interface ManageMemberRoleUseCase {
    String changeMemberRole(String memberId, ChangeMemberRoleRequest request);
}
