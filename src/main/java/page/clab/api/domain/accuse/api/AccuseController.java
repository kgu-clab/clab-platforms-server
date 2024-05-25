package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.AccuseService;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.domain.accuse.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/accuses")
@RequiredArgsConstructor
@Tag(name = "Accuse", description = "신고")
@Slf4j
public class AccuseController {

    private final AccuseService accuseService;

    @Operation(summary = "[U] 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createAccuse(
            @Valid @RequestBody AccuseRequestDto requestDto
    ) {
        Long id = accuseService.createAccuse(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 신고 내역 조회(신고 대상, 처리 상태 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "신고 대상, 처리 상태 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "누적 횟수 기준으로 정렬할지 여부를 선택할 수 있음")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<AccuseResponseDto>> getAccusesByConditions(
            @RequestParam(name = "targetType", required = false) TargetType type,
            @RequestParam(name = "accuseStatus", required = false) AccuseStatus status,
            @RequestParam(name = "countOrder", defaultValue = "false") boolean countOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("createAt"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, Accuse.class);
        PagedResponseDto<AccuseResponseDto> accuses = accuseService.getAccusesByConditions(type, status, countOrder, pageable);
        return ApiResponse.success(accuses);
    }

    @Operation(summary = "[U] 나의 신고 내역 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my")
    public ApiResponse<PagedResponseDto<AccuseMyResponseDto>> getMyAccuses(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("createAt"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, Accuse.class);
        PagedResponseDto<AccuseMyResponseDto> accuses = accuseService.getMyAccuses(pageable);
        return ApiResponse.success(accuses);
    }

    @Operation(summary = "[A] 신고 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{targetType}/{targetId}")
    public ApiResponse<Long> updateAccuseStatus(
            @PathVariable(name = "targetType") TargetType type,
            @PathVariable(name = "targetId") Long targetId,
            @RequestParam(name = "accuseStatus") AccuseStatus status
    ) {
        Long id = accuseService.updateAccuseStatus(type, targetId, status);
        return ApiResponse.success(id);
    }

}
