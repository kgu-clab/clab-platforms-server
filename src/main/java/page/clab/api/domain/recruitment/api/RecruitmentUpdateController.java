package page.clab.api.domain.recruitment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.recruitment.application.port.in.UpdateRecruitmentUseCase;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
public class RecruitmentUpdateController {

    private final UpdateRecruitmentUseCase updateRecruitmentUseCase;

    @Operation(summary = "[S] 모집 공고 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{recruitmentId}")
    public ApiResponse<Long> updateRecruitment(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @Valid @RequestBody RecruitmentUpdateRequestDto requestDto
    ) {
        Long id = updateRecruitmentUseCase.updateRecruitment(recruitmentId, requestDto);
        return ApiResponse.success(id);
    }
}
