package page.clab.api.domain.members.membershipFee.application.port.in;

import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeUpdateRequestDto;

public interface UpdateMembershipFeeUseCase {

    Long updateMembershipFee(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto);
}
