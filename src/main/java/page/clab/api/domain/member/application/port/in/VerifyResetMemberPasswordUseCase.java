package page.clab.api.domain.member.application.port.in;

import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

public interface VerifyResetMemberPasswordUseCase {
    String verify(VerificationRequestDto requestDto);
}
