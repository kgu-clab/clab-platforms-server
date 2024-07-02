package page.clab.api.domain.award.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.award.application.AwardRegisterUseCase;
import page.clab.api.domain.award.dto.request.AwardRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Award", description = "수상 이력")
public class AwardRegisterController {

    private final AwardRegisterUseCase awardRegisterUseCase;

    @Operation(summary = "[U] 수상 이력 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> registerAward(
            @Valid @RequestBody AwardRequestDto requestDto
    ) {
        Long id = awardRegisterUseCase.register(requestDto);
        return ApiResponse.success(id);
    }
}