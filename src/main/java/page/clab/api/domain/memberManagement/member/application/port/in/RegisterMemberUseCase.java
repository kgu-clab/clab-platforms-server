package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberRequestDto;

public interface RegisterMemberUseCase {

    String registerMember(MemberRequestDto requestDto);
}
