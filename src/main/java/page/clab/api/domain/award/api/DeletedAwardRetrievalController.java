package page.clab.api.domain.award.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.award.application.port.in.RetrieveDeletedAwardsUseCase;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Award", description = "수상 이력")
@Slf4j
public class DeletedAwardRetrievalController {

    private final RetrieveDeletedAwardsUseCase retrieveDeletedAwardsUseCase;

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 수상이력 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<AwardResponseDto>> retrieveDeletedAwards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> awards = retrieveDeletedAwardsUseCase.retrieveDeletedAwards(pageable);
        return ApiResponse.success(awards);
    }
}
