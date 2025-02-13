package page.clab.api.domain.memberManagement.workExperience.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.RetrieveMyWorkExperienceUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "Member Management - Work Experience", description = "경력사항")
public class MyWorkExperienceRetrievalController {

    private final RetrieveMyWorkExperienceUseCase retrieveMyWorkExperienceUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 나의 경력사항 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<WorkExperienceResponseDto>> retrieveMyWorkExperience(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "startDate") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection,
            WorkExperienceResponseDto.class);
        PagedResponseDto<WorkExperienceResponseDto> myWorkExperience = retrieveMyWorkExperienceUseCase.retrieveMyWorkExperience(
            pageable);
        return ApiResponse.success(myWorkExperience);
    }
}
