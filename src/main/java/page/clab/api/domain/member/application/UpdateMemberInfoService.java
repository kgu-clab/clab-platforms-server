package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateMemberInfoService {
    String updateMemberInfo(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException;
}