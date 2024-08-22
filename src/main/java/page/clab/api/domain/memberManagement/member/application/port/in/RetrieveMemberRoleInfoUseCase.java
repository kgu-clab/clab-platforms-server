package page.clab.api.domain.memberManagement.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberRoleInfoResponseDto;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMemberRoleInfoUseCase {
    PagedResponseDto<MemberRoleInfoResponseDto> retrieveMemberRoleInfo(String memberId, String memberName, Role role, Pageable pageable);
}
