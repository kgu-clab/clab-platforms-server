package page.clab.api.domain.memberManagement.member.application.service;

import jakarta.servlet.http.HttpServletRequest;
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
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberRoleManagementService implements ManageMemberRoleUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final UpdateMemberPort updateMemberPort;
    private final SlackService slackService;

    @Transactional
    @Override
    public String changeMemberRole(HttpServletRequest httpServletRequest, String memberId, ChangeMemberRoleRequest request) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);

        Role oldRole = member.getRole();
        Role newRole = request.getRole();

        validateRoleChange(member, newRole);
        member.changeRole(newRole);

        updateMemberPort.update(member);
        slackService.sendSecurityAlertNotification(httpServletRequest, SecurityAlertType.MEMBER_ROLE_CHANGED,
                String.format("[%s] %s: %s -> %s",
                        member.getId(), member.getName(), oldRole, newRole));
        return memberId;
    }

    private void validateRoleChange(Member member, Role role) {
        if (member.isGuestRole() || role.isGuestRole()) {
            throw new InvalidRoleChangeException("GUEST 권한은 변경 불가능합니다.");
        }
    }
}
