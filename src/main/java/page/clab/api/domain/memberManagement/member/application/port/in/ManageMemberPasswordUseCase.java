package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.application.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

public interface ManageMemberPasswordUseCase {

    String resendMemberPassword(String memberId);

    String requestMemberPasswordReset(MemberResetPasswordRequestDto requestDto);

    String verifyMemberPasswordReset(VerificationRequestDto requestDto);

    String generateOrRetrievePassword(String password);
}
