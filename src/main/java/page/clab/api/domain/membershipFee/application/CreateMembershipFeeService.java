package page.clab.api.domain.membershipFee.application;

import page.clab.api.domain.membershipFee.dto.request.MembershipFeeRequestDto;

public interface CreateMembershipFeeService {
    Long execute(MembershipFeeRequestDto requestDto);
}
