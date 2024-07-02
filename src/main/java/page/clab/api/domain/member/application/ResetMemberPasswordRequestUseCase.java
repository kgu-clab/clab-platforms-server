package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;

public interface ResetMemberPasswordRequestUseCase {
    String request(MemberResetPasswordRequestDto requestDto);
}
