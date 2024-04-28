package page.clab.api.domain.position.api;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.position.application.PositionService;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.domain.position.dto.response.PositionMyResponseDto;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Position", description = "멤버 직책")
@Slf4j
public class PositionController {

    private final PositionService positionService;

    @Operation(summary = "[S] 직책 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createPosition(
            @Valid @RequestBody PositionRequestDto requestDto
    ) {
        Long id = positionService.createPosition(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 연도/직책별 목록 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "연도, 직책 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<PositionResponseDto>> getPositionsByConditions(
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "positionType", required = false) PositionType positionType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<PositionResponseDto> positions = positionService.getPositionsByConditions(year, positionType, pageable);
        return ApiResponse.success(positions);
    }

    @Operation(summary = "[U] 나의 직책 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-positions")
    public ApiResponse<PositionMyResponseDto> getMyPositionsByYear(
            @RequestParam(name = "year", required = false) String year
    ) {
        PositionMyResponseDto positions = positionService.getMyPositionsByYear(year);
        return ApiResponse.success(positions);
    }

    @Operation(summary = "[S] 직책 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{positionId}")
    public ApiResponse<Long> deletePosition(
            @PathVariable("positionId") Long positionId
    ) {
        Long id = positionService.deletePosition(positionId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 직책 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<PositionResponseDto>> getDeletedPositions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<PositionResponseDto> positions = positionService.getDeletedPositions(pageable);
        return ApiResponse.success(positions);
    }

}
