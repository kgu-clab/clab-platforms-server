package page.clab.api.domain.memberManagement.cloud.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveCloudUsageByMemberIdUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/clouds")
@RequiredArgsConstructor
@Tag(name = "Member Management - Cloud", description = "클라우드")
public class CloudUsageRetrievalByMemberIdController {

    private final RetrieveCloudUsageByMemberIdUseCase retrieveCloudUsageByMemberIdUseCase;

    @Operation(summary = "[G] 멤버의 클라우드 사용량 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "본인 외의 정보는 ROLE_SUPER만 가능")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/{memberId}")
    public ApiResponse<CloudUsageInfo> retrieveCloudUsageByMemberId(
        @PathVariable(name = "memberId") String memberId
    ) {
        CloudUsageInfo usage = retrieveCloudUsageByMemberIdUseCase.retrieveCloudUsage(memberId);
        return ApiResponse.success(usage);
    }
}
