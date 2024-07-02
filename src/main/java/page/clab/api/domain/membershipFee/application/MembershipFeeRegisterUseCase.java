package page.clab.api.domain.membershipFee.application;

import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;

public interface MembershipFeeRegisterUseCase {
    Long register(MembershipFeeRequestDto requestDto);
}
