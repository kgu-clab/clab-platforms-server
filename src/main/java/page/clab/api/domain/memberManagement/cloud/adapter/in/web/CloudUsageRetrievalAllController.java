package page.clab.api.domain.memberManagement.cloud.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.domain.memberManagement.cloud.application.port.in.RetrieveAllCloudUsageUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/clouds")
@RequiredArgsConstructor
@Tag(name = "Member Management - Cloud", description = "클라우드")
public class CloudUsageRetrievalAllController {

    private final RetrieveAllCloudUsageUseCase retrieveAllCloudUsageUseCase;

    @Operation(summary = "[S] 모든 멤버의 클라우드 사용량 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<CloudUsageInfo>> retrieveAllCloudUsages(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CloudUsageInfo> cloudUsageInfos = retrieveAllCloudUsageUseCase.retrieveAllCloudUsages(
            pageable);
        return ApiResponse.success(cloudUsageInfos);
    }
}
