package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.request.ChangeMemberRoleRequest;
import page.clab.api.domain.memberManagement.member.application.exception.InvalidRoleChangeException;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberRoleUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.UpdateMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;

@Service
@RequiredArgsConstructor
public class MemberRoleManagementService implements ManageMemberRoleUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final UpdateMemberPort updateMemberPort;

    @Transactional
    @Override
    public String changeMemberRole(String memberId, ChangeMemberRoleRequest request) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);
        Role role = request.getRole();
        validateRoleChange(member, role);
        member.changeRole(role);
        return updateMemberPort.update(member).getId();
    }

    private void validateRoleChange(Member member, Role role) {
        if (member.isGuestRole() || role.isGuestRole()) {
            throw new InvalidRoleChangeException("GUEST 권한은 변경 불가능합니다.");
        }
    }
}
