package page.clab.api.domain.workExperience.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.workExperience.application.WorkExperienceService;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience", description = "경력사항")
@Slf4j
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @Operation(summary = "[U] 경력사항 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createWorkExperience(
            @Valid @RequestBody WorkExperienceRequestDto requestDto
    ) {
        Long id = workExperienceService.createWorkExperience(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 나의 경력사항 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<WorkExperienceResponseDto>> getMyWorkExperience(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        List<String> sortByList = sortBy.orElse(List.of("startDate"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, WorkExperience.class);
        PagedResponseDto<WorkExperienceResponseDto> myWorkExperience = workExperienceService.getMyWorkExperience(pageable);
        return ApiResponse.success(myWorkExperience);
    }

    @Operation(summary = "[U] 멤버의 경력사항 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<WorkExperienceResponseDto>> getWorkExperiencesByConditions(
            @RequestParam String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        List<String> sortByList = sortBy.orElse(List.of("startDate"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, WorkExperience.class);
        PagedResponseDto<WorkExperienceResponseDto> workExperiences = workExperienceService.getWorkExperiencesByConditions(memberId, pageable);
        return ApiResponse.success(workExperiences);
    }

    @Operation(summary = "[U] 경력사항 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{workExperienceId}")
    public ApiResponse<Long> updateWorkExperience(
            @PathVariable(name = "workExperienceId") Long workExperienceId,
            @Valid @RequestBody WorkExperienceUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = workExperienceService.updateWorkExperience(workExperienceId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 경력사항 삭제", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{workExperienceId}")
    public ApiResponse<Long> deleteWorkExperience(
            @PathVariable(name = "workExperienceId") Long workExperienceId
    ) throws PermissionDeniedException {
        Long id = workExperienceService.deleteWorkExperience(workExperienceId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 경력사항 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<WorkExperienceResponseDto>> getDeletedWorkExperiences(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<WorkExperienceResponseDto> workExperiences = workExperienceService.getDeletedWorkExperiences(pageable);
        return ApiResponse.success(workExperiences);
    }

}
