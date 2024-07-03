package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.dto.request.MemberRequestDto;

public interface RegisterMemberUseCase {
    String register(MemberRequestDto requestDto);
}
