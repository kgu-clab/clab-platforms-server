package page.clab.api.domain.memberManagement.position.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.position.application.dto.request.PositionRequestDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RegisterPositionUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Member Management - Position", description = "멤버 직책")
public class PositionRegisterController {

    private final RegisterPositionUseCase registerPositionUseCase;

    @Operation(summary = "[S] 직책 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @PostMapping("")
    public ApiResponse<Long> registerPosition(
            @Valid @RequestBody PositionRequestDto requestDto
    ) {
        Long id = registerPositionUseCase.registerPosition(requestDto);
        return ApiResponse.success(id);
    }
}
