package page.clab.api.domain.members.membershipFee.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.membershipFee.application.port.in.RemoveMembershipFeeUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "Members - Membership Fee", description = "회비")
public class MembershipFeeRemoveController {

    private final RemoveMembershipFeeUseCase removeMembershipFeeUseCase;

    @Operation(summary = "[S] 회비 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @DeleteMapping("/{membershipFeeId}")
    public ApiResponse<Long> removeMembershipFee(
        @PathVariable(name = "membershipFeeId") Long membershipFeeId
    ) {
        Long id = removeMembershipFeeUseCase.removeMembershipFee(membershipFeeId);
        return ApiResponse.success(id);
    }
}
