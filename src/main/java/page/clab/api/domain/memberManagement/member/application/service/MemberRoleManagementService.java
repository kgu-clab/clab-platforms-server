package page.clab.api.domain.memberManagement.member.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.request.ChangeMemberRoleRequest;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberRoleUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.UpdateMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberRoleManagementService implements ManageMemberRoleUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final UpdateMemberPort updateMemberPort;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String changeMemberRole(HttpServletRequest httpServletRequest, String memberId,
        ChangeMemberRoleRequest request) {
        Member member = retrieveMemberPort.getById(memberId);

        Role oldRole = member.getRole();
        Role newRole = request.getRole();

        validateRoleChange(member, oldRole, newRole);
        member.changeRole(newRole);

        updateMemberPort.update(member);

        String memberRoleChangedMessage = String.format("[%s] %s: %s -> %s", member.getId(), member.getName(), oldRole,
            newRole);
        eventPublisher.publishEvent(
            new NotificationEvent(this, SecurityAlertType.MEMBER_ROLE_CHANGED, httpServletRequest,
                memberRoleChangedMessage));

        return memberId;
    }

    private void validateRoleChange(Member member, Role oldRole, Role newRole) {
        // 기존 권한과 새 권한이 동일한지 확인
        if (oldRole.equals(newRole)) {
            throw new BaseException(ErrorCode.INVALID_ROLE_CHANGE, "기존 권한과 변경할 권한이 같습니다.");
        }
        // 멤버의 변경될 권한이 GUEST 인지 또는 변경되는 권한이 GUEST 인지 확인
        if (member.isGuestRole() || newRole.isGuestRole()) {
            throw new BaseException(ErrorCode.INVALID_ROLE_CHANGE, "GUEST 권한은 변경 불가능합니다.");
        }
    }
}
