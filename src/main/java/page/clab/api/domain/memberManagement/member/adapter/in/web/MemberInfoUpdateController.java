package page.clab.api.domain.memberManagement.member.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberUpdateRequestDto;
import page.clab.api.domain.memberManagement.member.application.port.in.UpdateMemberUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management - Member", description = "멤버")
public class MemberInfoUpdateController {

    private final UpdateMemberUseCase updateMemberUseCase;

    @Operation(summary = "[U] 멤버 정보 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
        "본인 외의 정보는 ROLE_SUPER만 가능")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{memberId}")
    public ApiResponse<String> updateMemberInfo(
        @PathVariable(name = "memberId") String memberId,
        @RequestBody MemberUpdateRequestDto requestDto
    ) {
        String id = updateMemberUseCase.updateMember(memberId, requestDto);
        return ApiResponse.success(id);
    }
}
