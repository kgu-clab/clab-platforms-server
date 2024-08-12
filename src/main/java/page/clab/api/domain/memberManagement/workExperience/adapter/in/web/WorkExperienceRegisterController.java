package page.clab.api.domain.memberManagement.workExperience.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RegisterWorkExperienceUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "Member Management - Work Experience", description = "경력사항")
public class WorkExperienceRegisterController {

    private final RegisterWorkExperienceUseCase registerWorkExperienceUseCase;

    @Operation(summary = "[U] 경력사항 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<Long> registerWorkExperience(
            @Valid @RequestBody WorkExperienceRequestDto requestDto
    ) {
        Long id = registerWorkExperienceUseCase.registerWorkExperience(requestDto);
        return ApiResponse.success(id);
    }
}
