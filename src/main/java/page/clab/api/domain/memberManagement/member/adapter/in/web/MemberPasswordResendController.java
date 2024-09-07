package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.port.in.ResendMemberPasswordUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberPasswordResendController {

    private final ResendMemberPasswordUseCase resendMemberPasswordUseCase;

    @Operation(summary = "[S] 비밀번호 재전송", description = "비밀번호 재전송")
    @PreAuthorize("hasRole('SUPER')")
    @PostMapping("/{memberId}/password/resend")
    public ApiResponse<String> resendMemberPassword(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = resendMemberPasswordUseCase.resendMemberPassword(memberId);
        return ApiResponse.success(id);
    }
}
