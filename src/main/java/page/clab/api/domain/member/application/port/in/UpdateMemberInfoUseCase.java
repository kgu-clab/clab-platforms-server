package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateMemberInfoUseCase {
    String update(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException;
}
