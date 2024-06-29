package page.clab.api.domain.membershipFee.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchMembershipFeesByConditionsService {
    PagedResponseDto<MembershipFeeResponseDto> execute(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable);
}