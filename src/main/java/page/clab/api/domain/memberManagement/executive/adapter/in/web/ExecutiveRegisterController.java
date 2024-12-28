package page.clab.api.domain.memberManagement.executive.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveRequestDto;
import page.clab.api.domain.memberManagement.executive.application.port.in.RegisterExecutiveUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/executive")
@RequiredArgsConstructor
@Tag(name = "Member Management - Executive", description = "운영진")
public class ExecutiveRegisterController {

    private final RegisterExecutiveUseCase registerExecutiveUseCase;

    @Operation(summary = "[A] 운영진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
        "position은 다음과 같이 등록 가능<br>" +
        "- PRESIDENT : 회장<br>" +
        "- VICE_PRESIDENT : 부회장<br>" +
        "- GENERAL : 일반 운영진<br>")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<String> registerExecutive(
        @Valid @RequestBody ExecutiveRequestDto requestDto
    ) {
        String id = registerExecutiveUseCase.registerExecutive(requestDto);
        return ApiResponse.success(id);
    }
}
