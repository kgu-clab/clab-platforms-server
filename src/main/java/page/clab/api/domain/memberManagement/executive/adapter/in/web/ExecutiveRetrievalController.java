package page.clab.api.domain.memberManagement.executive.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.executive.application.dto.response.ExecutiveResponseDto;
import page.clab.api.domain.memberManagement.executive.application.port.in.RetrieveExecutiveUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/executive")
@RequiredArgsConstructor
@Tag(name = "Member Management - Executive", description = "운영진")
public class ExecutiveRetrievalController {

    private final RetrieveExecutiveUseCase retrieveExecutiveUseCase;

    @Operation(summary = "운영진 정보 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("")
    public ApiResponse<List<ExecutiveResponseDto>> retrieveExecutives() {
        List<ExecutiveResponseDto> executives = retrieveExecutiveUseCase.retrieveExecutives();
        return ApiResponse.success(executives);
    }
}
