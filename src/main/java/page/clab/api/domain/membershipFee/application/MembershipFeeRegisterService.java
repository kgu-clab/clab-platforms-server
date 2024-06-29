package page.clab.api.domain.membershipFee.application;

import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;

public interface MembershipFeeRegisterService {
    Long register(MembershipFeeRequestDto requestDto);
}
