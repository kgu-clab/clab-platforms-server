package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.port.in.VerifyResetMemberPasswordUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버 관련 API")
public class ResetMemberPasswordVerificationController {

    private final VerifyResetMemberPasswordUseCase verifyResetMemberPasswordUseCase;

    @Operation(summary = "멤버 비밀번호 재발급 인증", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password-reset-verifications")
    public ApiResponse<String> verifyResetMemberPassword(
            @RequestBody VerificationRequestDto requestDto
    ) {
        String id = verifyResetMemberPasswordUseCase.verifyResetMemberPassword(requestDto);
        return ApiResponse.success(id);
    }
}
