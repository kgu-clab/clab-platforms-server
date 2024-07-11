package page.clab.api.domain.hiring.application.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.hiring.application.application.port.in.ApplyForApplicationUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Hiring - Application", description = "동아리 지원 관련 API")
public class ApplicationApplyController {

    private final ApplyForApplicationUseCase applyForApplicationUseCase;

    @Operation(summary = "동아리 지원", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("")
    public ApiResponse<String> applyForClub(
            @Valid @RequestBody ApplicationRequestDto requestDto
    ) {
        String id = applyForApplicationUseCase.applyForClub(requestDto);
        return ApiResponse.success(id);
    }
}
