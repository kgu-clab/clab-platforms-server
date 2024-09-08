package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberPasswordUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberPasswordController {

    private final ManageMemberPasswordUseCase manageMemberPasswordUseCase;

    @Operation(summary = "[S] 멤버 비밀번호 재전송", description = "비밀번호 재전송")
    @PreAuthorize("hasRole('SUPER')")
    @PostMapping("/password/{memberId}/resend")
    public ApiResponse<String> resendMemberPassword(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = manageMemberPasswordUseCase.resendMemberPassword(memberId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "멤버 비밀번호 재발급 요청", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password/reset-requests")
    public ApiResponse<String> requestResetMemberPassword(
            @RequestBody MemberResetPasswordRequestDto requestDto
    ) {
        String id = manageMemberPasswordUseCase.requestMemberPasswordReset(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "멤버 비밀번호 재발급 인증", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password/reset-verifications")
    public ApiResponse<String> verifyResetMemberPassword(
            @RequestBody VerificationRequestDto requestDto
    ) {
        String id = manageMemberPasswordUseCase.verifyMemberPasswordReset(requestDto);
        return ApiResponse.success(id);
    }
}
