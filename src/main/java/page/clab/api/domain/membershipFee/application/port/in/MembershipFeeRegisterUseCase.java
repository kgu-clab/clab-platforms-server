package page.clab.api.domain.membershipFee.application.port.in;

import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;

public interface MembershipFeeRegisterUseCase {
    Long register(MembershipFeeRequestDto requestDto);
}
