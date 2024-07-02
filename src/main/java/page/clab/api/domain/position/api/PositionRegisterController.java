package page.clab.api.domain.position.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.position.application.port.in.PositionRegisterUseCase;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@Tag(name = "Position", description = "멤버 직책")
public class PositionRegisterController {

    private final PositionRegisterUseCase positionRegisterUseCase;

    @Operation(summary = "[S] 직책 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> registerPosition(
            @Valid @RequestBody PositionRequestDto requestDto
    ) {
        Long id = positionRegisterUseCase.register(requestDto);
        return ApiResponse.success(id);
    }
}
