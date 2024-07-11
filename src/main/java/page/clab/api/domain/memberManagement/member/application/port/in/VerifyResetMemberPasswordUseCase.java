package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

public interface VerifyResetMemberPasswordUseCase {
    String verifyResetMemberPassword(VerificationRequestDto requestDto);
}
