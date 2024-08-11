package page.clab.api.domain.memberManagement.position.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionResponseDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RetrievePositionsByConditionsUseCase;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Member Management - Position", description = "멤버 직책")
public class PositionsByConditionsRetrievalController {

    private final RetrievePositionsByConditionsUseCase retrievePositionsByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 연도/직책별 목록 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "연도, 직책 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<PositionResponseDto>> retrievePositionsByConditions(
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "positionType", required = false) PositionType positionType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "year, positionType") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc, asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, PositionResponseDto.class);
        PagedResponseDto<PositionResponseDto> positions = retrievePositionsByConditionsUseCase.retrievePositions(year, positionType, pageable);
        return ApiResponse.success(positions);
    }
}
