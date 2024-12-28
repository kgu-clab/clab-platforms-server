package page.clab.api.domain.memberManagement.workExperience.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RetrieveDeletedWorkExperiencesUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "Member Management - Work Experience", description = "경력사항")
public class DeletedWorkExperiencesRetrievalController {

    private final RetrieveDeletedWorkExperiencesUseCase retrieveDeletedWorkExperiencesUseCase;

    @Operation(summary = "[S] 삭제된 경력사항 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("/deleted")
    public ApiResponse<PagedResponseDto<WorkExperienceResponseDto>> retrieveDeletedWorkExperiences(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<WorkExperienceResponseDto> workExperiences =
            retrieveDeletedWorkExperiencesUseCase.retrieveDeletedWorkExperiences(pageable);
        return ApiResponse.success(workExperiences);
    }
}
