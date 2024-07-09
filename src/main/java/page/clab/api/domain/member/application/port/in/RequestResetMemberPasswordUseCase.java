package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.application.dto.request.MemberResetPasswordRequestDto;

public interface RequestResetMemberPasswordUseCase {
    String requestResetMemberPassword(MemberResetPasswordRequestDto requestDto);
}
