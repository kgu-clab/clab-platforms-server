package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.port.in.RemoveMemberUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
public class MemberRemoveController {

    private final RemoveMemberUseCase removeMemberUseCase;

    @Operation(summary = "[S] 멤버 정보 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @DeleteMapping("/{memberId}")
    public ApiResponse<String> removeMember(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = removeMemberUseCase.removeMember(memberId);
        return ApiResponse.success(id);
    }
}
