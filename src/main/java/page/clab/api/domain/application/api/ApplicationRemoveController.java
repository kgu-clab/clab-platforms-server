package page.clab.api.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.application.ApplicationRemoveUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application", description = "동아리 지원")
public class ApplicationRemoveController {

    private final ApplicationRemoveUseCase applicationRemoveUseCase;

    @Operation(summary = "[S] 지원서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}/{studentId}")
    public ApiResponse<String> removeApplication(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        String id = applicationRemoveUseCase.remove(recruitmentId, studentId);
        return ApiResponse.success(id);
    }
}