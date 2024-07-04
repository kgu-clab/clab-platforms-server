package page.clab.api.domain.membershipFee.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.membershipFee.application.port.in.UpdateMembershipFeeUseCase;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "MembershipFee", description = "회비")
public class MembershipFeeUpdateController {

    private final UpdateMembershipFeeUseCase updateMembershipFeeUseCase;

    @Operation(summary = "[S] 회비 정보 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{membershipFeeId}")
    public ApiResponse<Long> updateMembershipFee(
            @PathVariable(name = "membershipFeeId") Long membershipFeeId,
            @Valid @RequestBody MembershipFeeUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = updateMembershipFeeUseCase.updateMembershipFee(membershipFeeId, requestDto);
        return ApiResponse.success(id);
    }
}
