package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.FetchCloudUsageByMemberIdService;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/member-clouds")
@RequiredArgsConstructor
@Tag(name = "Member Cloud", description = "멤버 클라우드")
public class FetchCloudUsageByMemberIdController {

    private final FetchCloudUsageByMemberIdService fetchCloudUsageByMemberIdService;

    @Operation(summary = "[U] 멤버의 클라우드 사용량 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{memberId}")
    public ApiResponse<CloudUsageInfo> fetchCloudUsageByMemberId(
            @PathVariable(name = "memberId") String memberId
    ) throws PermissionDeniedException {
        CloudUsageInfo usage = fetchCloudUsageByMemberIdService.execute(memberId);
        return ApiResponse.success(usage);
    }
}
