package page.clab.api.domain.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMemberBirthdaysThisMonthUseCase {
    PagedResponseDto<MemberBirthdayResponseDto> retrieve(int month, Pageable pageable);
}
