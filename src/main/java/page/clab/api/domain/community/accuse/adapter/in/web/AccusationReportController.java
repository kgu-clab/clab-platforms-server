package page.clab.api.domain.community.accuse.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.accuse.application.dto.request.AccuseRequestDto;
import page.clab.api.domain.community.accuse.application.port.in.ReportAccusationUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Community - Accusation", description = "커뮤니티 신고")
public class AccusationReportController {

    private final ReportAccusationUseCase reportAccusationUsecase;

    @Operation(summary = "[U] 신고 접수", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<Long> reportIncident(
            @Valid @RequestBody AccuseRequestDto requestDto
    ) {
        Long id = reportAccusationUsecase.reportAccusation(requestDto);
        return ApiResponse.success(id);
    }
}
