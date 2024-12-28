package page.clab.api.domain.memberManagement.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberBirthdayResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMemberBirthdaysThisMonthUseCase {

    PagedResponseDto<MemberBirthdayResponseDto> retrieveBirthdaysThisMonth(int month, Pageable pageable);
}
