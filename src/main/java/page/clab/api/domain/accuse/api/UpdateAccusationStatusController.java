package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.UpdateAccusationStatusService;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Accusation", description = "신고")
@Slf4j
public class UpdateAccusationStatusController {

    private final UpdateAccusationStatusService updateAccusationStatusService;

    @Operation(summary = "[A] 신고 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{targetType}/{targetId}")
    public ApiResponse<Long> updateAccusationStatus(
            @PathVariable(name = "targetType") TargetType type,
            @PathVariable(name = "targetId") Long targetId,
            @RequestParam(name = "accuseStatus") AccuseStatus status
    ) {
        Long id = updateAccusationStatusService.execute(type, targetId, status);
        return ApiResponse.success(id);
    }
}