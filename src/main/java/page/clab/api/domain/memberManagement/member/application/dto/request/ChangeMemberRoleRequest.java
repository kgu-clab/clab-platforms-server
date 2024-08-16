package page.clab.api.domain.memberManagement.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.domain.Role;

@Getter
@Setter
public class ChangeMemberRoleRequest {

    @Schema(description = "변경할 멤버 권한", example = "USER")
    private Role role;
}
