package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateMemberUseCase {
    String updateMember(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException;
}
