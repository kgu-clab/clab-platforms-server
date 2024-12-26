package page.clab.api.domain.members.membershipFee.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.membershipFee.application.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedMembershipFeesUseCase {

    PagedResponseDto<MembershipFeeResponseDto> retrieveDeletedMembershipFees(Pageable pageable);
}
