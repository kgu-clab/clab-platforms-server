package page.clab.api.domain.award.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.award.application.AwardUpdateService;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Award", description = "수상 이력")
@Slf4j
public class AwardUpdateController {

    private final AwardUpdateService awardUpdateService;

    @Operation(summary = "[U] 수상 이력 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{awardId}")
    public ApiResponse<Long> updateAward(
            @PathVariable(name = "awardId") Long awardId,
            @Valid @RequestBody AwardUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = awardUpdateService.update(awardId, requestDto);
        return ApiResponse.success(id);
    }
}