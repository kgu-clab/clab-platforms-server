package page.clab.api.domain.members.membershipFee.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMembershipFeesByConditionsUseCase {
    PagedResponseDto<MembershipFeeResponseDto> retrieveMembershipFees(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable);
}
