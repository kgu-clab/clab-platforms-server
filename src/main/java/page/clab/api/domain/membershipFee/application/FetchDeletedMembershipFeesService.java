package page.clab.api.domain.membershipFee.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchDeletedMembershipFeesService {
    PagedResponseDto<MembershipFeeResponseDto> execute(Pageable pageable);
}