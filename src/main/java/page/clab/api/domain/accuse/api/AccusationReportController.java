package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.port.in.ReportAccusationUseCase;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Accusation", description = "신고")
public class AccusationReportController {

    private final ReportAccusationUseCase reportAccusationUsecase;

    @Operation(summary = "[U] 신고 접수", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> reportIncident(
            @Valid @RequestBody AccuseRequestDto requestDto
    ) {
        Long id = reportAccusationUsecase.reportIncident(requestDto);
        return ApiResponse.success(id);
    }
}
