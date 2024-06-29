package page.clab.api.domain.membershipFee.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.membershipFee.application.MembershipFeeRemoveService;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee", description = "회비")
public class MembershipFeeRemoveController {

    private final MembershipFeeRemoveService membershipFeeRemoveService;

    @Operation(summary = "[S] 회비 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{membershipFeeId}")
    public ApiResponse<Long> removeMembershipFee(
            @PathVariable(name = "membershipFeeId") Long membershipFeeId
    ) throws PermissionDeniedException {
        Long id = membershipFeeRemoveService.remove(membershipFeeId);
        return ApiResponse.success(id);
    }
}
