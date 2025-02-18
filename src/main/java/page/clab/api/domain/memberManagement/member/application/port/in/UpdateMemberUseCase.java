package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberUpdateRequestDto;

public interface UpdateMemberUseCase {

    String updateMember(String memberId, MemberUpdateRequestDto requestDto);
}
