package page.clab.api.domain.memberManagement.workExperience.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RemoveWorkExperienceUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "Member Management - Work Experience", description = "경력사항")
public class WorkExperienceRemoveController {

    private final RemoveWorkExperienceUseCase removeWorkExperienceUseCase;

    @Operation(summary = "[U] 경력사항 삭제", description = "ROLE_USER 이상의 권한이 필요함<br>" +
        "본인 외의 정보는 ROLE_SUPER만 가능")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{workExperienceId}")
    public ApiResponse<Long> removeWorkExperience(
        @PathVariable(name = "workExperienceId") Long workExperienceId
    ) throws PermissionDeniedException {
        Long id = removeWorkExperienceUseCase.removeWorkExperience(workExperienceId);
        return ApiResponse.success(id);
    }
}
