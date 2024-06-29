package page.clab.api.domain.login.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.MemberBanService;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/account-lock-info")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인")
public class MemberBanController {

    private final MemberBanService memberBanService;

    @Operation(summary = "[S] 멤버 밴 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("/ban/{memberId}")
    public ApiResponse<Long> banMember(
            HttpServletRequest request,
            @PathVariable(name = "memberId") String memberId
    ) {
        Long id = memberBanService.ban(request, memberId);
        return ApiResponse.success(id);
    }
}