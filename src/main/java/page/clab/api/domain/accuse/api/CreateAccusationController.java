package page.clab.api.domain.accuse.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.accuse.application.CreateAccusationService;
import page.clab.api.domain.accuse.dto.request.AccuseRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/accusations")
@RequiredArgsConstructor
@Tag(name = "Accusation", description = "신고")
@Slf4j
public class CreateAccusationController {

    private final CreateAccusationService createAccusationService;

    @Operation(summary = "[U] 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createAccusation(
            @Valid @RequestBody AccuseRequestDto requestDto
    ) {
        Long id = createAccusationService.execute(requestDto);
        return ApiResponse.success(id);
    }
}
