package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MemberRegisterUseCase;
import page.clab.api.domain.member.dto.request.MemberRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
public class MemberRegisterController {

    private final MemberRegisterUseCase memberRegisterUseCase;

    @Operation(summary = "[S] 신규 멤버 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PostMapping("")
    public ApiResponse<String> registerMember(
            @RequestBody MemberRequestDto requestDto
    ) {
        String id = memberRegisterUseCase.register(requestDto);
        return ApiResponse.success(id);
    }
}
