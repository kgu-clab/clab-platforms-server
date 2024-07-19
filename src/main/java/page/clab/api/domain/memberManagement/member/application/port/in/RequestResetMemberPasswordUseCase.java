package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberResetPasswordRequestDto;

public interface RequestResetMemberPasswordUseCase {
    String requestResetMemberPassword(MemberResetPasswordRequestDto requestDto);
}
