package page.clab.api.domain.membershipFee.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMembershipFeesByConditionsUseCase {
    PagedResponseDto<MembershipFeeResponseDto> retrieve(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable);
}
