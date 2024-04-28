package page.clab.api.domain.recruitment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import page.clab.api.domain.recruitment.application.RecruitmentService;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
@Slf4j
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @Operation(summary = "[S] 모집 공고 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createRecruitment(
            @Valid @RequestBody RecruitmentRequestDto requestDto
    ) {
        Long id = recruitmentService.createRecruitment(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "모집 공고 목록(최근 5건)", description = "ROLE_ANONYMOUS 이상의 권한이 필요함<br>" +
            "최근 5건의 모집 공고를 조회")
    @GetMapping("")
    public ApiResponse<List<RecruitmentResponseDto>> getRecentRecruitments() {
        List<RecruitmentResponseDto> recruitments = recruitmentService.getRecentRecruitments();
        return ApiResponse.success(recruitments);
    }

    @Operation(summary = "[S] 모집 공고 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{recruitmentId}")
    public ApiResponse<Long> updateRecruitment(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @Valid @RequestBody RecruitmentUpdateRequestDto requestDto
    ) {
        Long id = recruitmentService.updateRecruitment(recruitmentId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[S] 모집 공고 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}")
    public ApiResponse<Long> deleteRecruitment(
            @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        Long id = recruitmentService.deleteRecruitment(recruitmentId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 모집 공고 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<RecruitmentResponseDto>> getDeletedRecruitments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<RecruitmentResponseDto> recruitments = recruitmentService.getDeletedRecruitments(pageable);
        return ApiResponse.success(recruitments);
    }

}
