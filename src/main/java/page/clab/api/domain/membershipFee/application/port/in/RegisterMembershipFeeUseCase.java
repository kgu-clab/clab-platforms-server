package page.clab.api.domain.membershipFee.application.port.in;

import page.clab.api.domain.membershipFee.application.dto.request.MembershipFeeRequestDto;

public interface RegisterMembershipFeeUseCase {
    Long registerMembershipFee(MembershipFeeRequestDto requestDto);
}
