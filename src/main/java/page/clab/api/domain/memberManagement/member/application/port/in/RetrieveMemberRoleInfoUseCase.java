package page.clab.api.domain.memberManagement.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.member.application.dto.response.MemberRoleInfoResponseDto;

import java.util.List;

public interface RetrieveMemberRoleInfoUseCase {
    List<MemberRoleInfoResponseDto> retrieveMemberRoleInfo(Pageable pageable);
}
