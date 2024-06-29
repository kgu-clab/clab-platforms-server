package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.AccusationRetrievalService;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Accusation", description = "신고")
public class AccusationRetrievalController {

    private final AccusationRetrievalService accusationRetrievalService;

    @Operation(summary = "[A] 신고 내역 조회(신고 대상, 처리 상태 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "신고 대상, 처리 상태 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "누적 횟수 기준으로 정렬할지 여부를 선택할 수 있음<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId, targetReferenceId")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<AccuseResponseDto>> retrieveAccusations(
            @RequestParam(name = "targetType", required = false) TargetType type,
            @RequestParam(name = "accuseStatus", required = false) AccuseStatus status,
            @RequestParam(name = "countOrder", defaultValue = "false") boolean countOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, AccuseTarget.class);
        PagedResponseDto<AccuseResponseDto> accuses = accusationRetrievalService.retrieveAccusations(type, status, countOrder, pageable);
        return ApiResponse.success(accuses);
    }
}