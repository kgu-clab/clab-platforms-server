package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.request.ChangeMemberRoleRequest;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberRoleUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberRoleManagementController {

    private final ManageMemberRoleUseCase manageMemberRoleUseCase;

    @Operation(summary = "[S] 멤버 권한 변경", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "권한 계층: SUPER > ADMIN > USER > GUEST<br>" +
            "GUEST 권한은 변경 불가능함")
    @PreAuthorize("hasRole('SUPER')")
    @PatchMapping("/{memberId}/roles")
    public ApiResponse<String> changeMemberRole(
            @PathVariable(name = "memberId") String memberId,
            @RequestBody ChangeMemberRoleRequest request
    ) {
        String id = manageMemberRoleUseCase.changeMemberRole(memberId, request);
        return ApiResponse.success(id);
    }
}
