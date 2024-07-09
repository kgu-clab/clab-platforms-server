package page.clab.api.domain.recruitment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.recruitment.application.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.recruitment.application.port.in.RegisterRecruitmentUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
public class RecruitmentRegisterController {

    private final RegisterRecruitmentUseCase registerRecruitmentUseCase;

    @Operation(summary = "[S] 모집 공고 등록", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({ "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerRecruitment(
            @Valid @RequestBody RecruitmentRequestDto requestDto
    ) {
        Long id = registerRecruitmentUseCase.registerRecruitment(requestDto);
        return ApiResponse.success(id);
    }
}
