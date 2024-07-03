package page.clab.api.domain.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMembersByConditionsUseCase {
    PagedResponseDto<MemberResponseDto> retrieve(String id, String name, Pageable pageable);
}
