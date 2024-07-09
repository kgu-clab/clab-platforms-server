package page.clab.api.domain.position.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.position.application.dto.response.PositionMyResponseDto;
import page.clab.api.domain.position.application.port.in.RetrieveMyPositionsByYearUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Position", description = "멤버 직책")
public class MyPositionsByYearRetrievalController {

    private final RetrieveMyPositionsByYearUseCase retrieveMyPositionsByYearUseCase;

    @Operation(summary = "[U] 나의 직책 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("/my-positions")
    public ApiResponse<PositionMyResponseDto> retrieveMyPositionsByYear(
            @RequestParam(name = "year", required = false) String year
    ) {
        PositionMyResponseDto positions = retrieveMyPositionsByYearUseCase.retrieveMyPositionsByYear(year);
        return ApiResponse.success(positions);
    }
}
