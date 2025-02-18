package page.clab.api.domain.memberManagement.award.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.award.application.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.memberManagement.award.application.port.in.UpdateAwardUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Member Management - Award", description = "수상 이력")
public class AwardUpdateController {

    private final UpdateAwardUseCase updateAwardUseCase;

    @Operation(summary = "[U] 수상 이력 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
        "본인 외의 정보는 ROLE_SUPER만 가능")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{awardId}")
    public ApiResponse<Long> updateAward(
        @PathVariable(name = "awardId") Long awardId,
        @Valid @RequestBody AwardUpdateRequestDto requestDto
    ) {
        Long id = updateAwardUseCase.updateAward(awardId, requestDto);
        return ApiResponse.success(id);
    }
}
