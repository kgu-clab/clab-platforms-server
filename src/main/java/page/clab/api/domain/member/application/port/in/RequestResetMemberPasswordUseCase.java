package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;

public interface RequestResetMemberPasswordUseCase {
    String request(MemberResetPasswordRequestDto requestDto);
}
