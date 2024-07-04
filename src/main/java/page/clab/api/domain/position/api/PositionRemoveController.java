package page.clab.api.domain.position.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.position.application.port.in.RemovePositionUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Position", description = "멤버 직책")
public class PositionRemoveController {

    private final RemovePositionUseCase removePositionUseCase;

    @Operation(summary = "[S] 직책 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({ "ROLE_SUPER" })
    @DeleteMapping("/{positionId}")
    public ApiResponse<Long> removePosition(
            @PathVariable("positionId") Long positionId
    ) {
        Long id = removePositionUseCase.removePosition(positionId);
        return ApiResponse.success(id);
    }
}
