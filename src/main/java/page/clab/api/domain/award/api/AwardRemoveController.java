package page.clab.api.domain.award.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.award.application.port.in.RemoveAwardUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Award", description = "수상 이력")
@Slf4j
public class AwardRemoveController {

    private final RemoveAwardUseCase removeAwardUseCase;

    @Operation(summary = "[U] 수상 이력 삭제", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @DeleteMapping("/{awardId}")
    public ApiResponse<Long> removeAward(
            @PathVariable(name = "awardId") Long awardId
    ) throws PermissionDeniedException {
        Long id = removeAwardUseCase.removeAward(awardId);
        return ApiResponse.success(id);
    }
}
