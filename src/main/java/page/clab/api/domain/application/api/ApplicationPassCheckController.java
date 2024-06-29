package page.clab.api.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.application.ApplicationPassCheckService;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application", description = "동아리 지원")
public class ApplicationPassCheckController {

    private final ApplicationPassCheckService applicationPassCheckService;

    @Operation(summary = "합격 여부 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{recruitmentId}/{studentId}")
    public ApiResponse<ApplicationPassResponseDto> checkApplicationPass(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        ApplicationPassResponseDto pass = applicationPassCheckService.checkPassStatus(recruitmentId, studentId);
        return ApiResponse.success(pass);
    }
}