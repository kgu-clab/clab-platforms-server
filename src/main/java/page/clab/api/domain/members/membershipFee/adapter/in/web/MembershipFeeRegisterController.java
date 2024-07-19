package page.clab.api.domain.members.membershipFee.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RegisterMembershipFeeUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/membership-fees")
@RequiredArgsConstructor
@Tag(name = "Members - Membership Fee", description = "회비")
public class MembershipFeeRegisterController {

    private final RegisterMembershipFeeUseCase registerMembershipFeeUseCase;

    @Operation(summary = "[U] 회비 신청", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerMembershipFee(
            @Valid @RequestBody MembershipFeeRequestDto requestDto
    ) {
        Long id = registerMembershipFeeUseCase.registerMembershipFee(requestDto);
        return ApiResponse.success(id);
    }
}
