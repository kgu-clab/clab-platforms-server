package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.port.in.ResetMemberPasswordRequestUseCase;
import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
public class ResetMemberPasswordRequestController {

    private final ResetMemberPasswordRequestUseCase resetMemberPasswordRequestUseCase;

    @Operation(summary = "멤버 비밀번호 재발급 요청", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("/password-reset-requests")
    public ApiResponse<String> requestResetMemberPassword(
            @RequestBody MemberResetPasswordRequestDto requestDto
    ) {
        String id = resetMemberPasswordRequestUseCase.request(requestDto);
        return ApiResponse.success(id);
    }
}
