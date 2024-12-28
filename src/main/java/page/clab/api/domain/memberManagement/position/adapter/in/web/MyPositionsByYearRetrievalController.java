package page.clab.api.domain.memberManagement.position.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionMyResponseDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RetrieveMyPositionsByYearUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Member Management - Position", description = "멤버 직책")
public class MyPositionsByYearRetrievalController {

    private final RetrieveMyPositionsByYearUseCase retrieveMyPositionsByYearUseCase;

    @Operation(summary = "[G] 나의 직책 조회", description = "ROLE_GUEST 이상의 권한이 필요함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/my-positions")
    public ApiResponse<PositionMyResponseDto> retrieveMyPositionsByYear(
        @RequestParam(name = "year", required = false) String year
    ) {
        PositionMyResponseDto positions = retrieveMyPositionsByYearUseCase.retrieveMyPositionsByYear(year);
        return ApiResponse.success(positions);
    }
}
