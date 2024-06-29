package page.clab.api.domain.member.application;

import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;

public interface ResetMemberPasswordRequestService {
    String request(MemberResetPasswordRequestDto requestDto);
}
