package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.application.dto.request.MemberRequestDto;

public interface RegisterMemberUseCase {
    String registerMember(MemberRequestDto requestDto);
}
