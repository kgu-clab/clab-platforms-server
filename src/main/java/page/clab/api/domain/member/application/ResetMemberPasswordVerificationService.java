package page.clab.api.domain.member.application;

import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

public interface ResetMemberPasswordVerificationService {
    String verify(VerificationRequestDto requestDto);
}
