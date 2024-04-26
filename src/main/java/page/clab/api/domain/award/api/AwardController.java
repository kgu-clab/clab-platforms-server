package page.clab.api.domain.award.api;

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
import page.clab.api.domain.award.application.AwardService;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Award", description = "수상 이력")
@Slf4j
public class AwardController {

    private final AwardService awardService;

    @Operation(summary = "[U] 수상 이력 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createAward(
            @Valid @RequestBody AwardRequestDto requestDto
    ) {
        Long id = awardService.createAward(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 수상 이력 조회(학번, 연도 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "학번, 연도 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<AwardResponseDto>> getAwardsByConditions(
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "year", required = false) Long year,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> awards = awardService.getAwardsByConditions(memberId, year, pageable);
        return ApiResponse.success(awards);
    }

    @Operation(summary = "[U] 나의 수상 이력 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<AwardResponseDto>> getMyAwards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AwardResponseDto> myAwards = awardService.getMyAwards(pageable);
        return ApiResponse.success(myAwards);
    }

    @Operation(summary = "[U] 수상 이력 수정", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{awardId}")
    public ApiResponse<Long> updateAward(
            @PathVariable(name = "awardId") Long awardId,
            @Valid @RequestBody AwardUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = awardService.updateAward(awardId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 수상 이력 삭제", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "본인 외의 정보는 ROLE_SUPER만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{awardId}")
    public ApiResponse<Long> deleteAward(
            @PathVariable(name = "awardId") Long awardId
    ) throws PermissionDeniedException {
        Long id = awardService.deleteAward(awardId);
        return ApiResponse.success(id);
    }

}