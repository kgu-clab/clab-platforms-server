package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateMemberInfoUseCase {
    String updateMemberInfo(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException;
}
