package page.clab.api.domain.memberManagement.award.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.award.application.dto.response.AwardResponseDto;
import page.clab.api.domain.memberManagement.award.application.port.in.RetrieveDeletedAwardsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Member Management - Award", description = "수상 이력 관련 API")
public class DeletedAwardRetrievalController {

    private final RetrieveDeletedAwardsUseCase retrieveDeletedAwardsUseCase;

    @GetMapping("/deleted")
    @Secured({ "ROLE_SUPER" })
    @Operation(summary = "[S] 삭제된 수상이력 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    public ApiResponse<PagedResponseDto<AwardResponseDto>> retrieveDeletedAwards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> awards = retrieveDeletedAwardsUseCase.retrieveDeletedAwards(pageable);
        return ApiResponse.success(awards);
    }
}
