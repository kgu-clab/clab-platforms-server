package page.clab.api.domain.auth.accountLockInfo.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.BanMemberUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/account-lock-info")
@RequiredArgsConstructor
@Tag(name = "Authentication - Account Lock Info", description = "계정 잠금 정보")
public class MemberBanController {

    private final BanMemberUseCase banMemberUseCase;

    @Operation(summary = "[S] 멤버 밴 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @PostMapping("/ban/{memberId}")
    public ApiResponse<Long> banMember(
            HttpServletRequest request,
            @PathVariable(name = "memberId") String memberId
    ) {
        Long id = banMemberUseCase.banMember(request, memberId);
        return ApiResponse.success(id);
    }
}
