package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberRequestDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RegisterMemberUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberRegisterController {

    private final RegisterMemberUseCase registerMemberUseCase;

    @Operation(summary = "[S] 신규 멤버 생성", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @PostMapping("")
    public ApiResponse<String> registerMember(
        @RequestBody MemberRequestDto requestDto
    ) {
        String id = registerMemberUseCase.registerMember(requestDto);
        return ApiResponse.success(id);
    }
}
