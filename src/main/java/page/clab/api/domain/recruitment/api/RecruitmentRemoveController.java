package page.clab.api.domain.recruitment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.recruitment.application.RecruitmentRemoveService;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
public class RecruitmentRemoveController {

    private final RecruitmentRemoveService recruitmentRemoveService;

    @Operation(summary = "[S] 모집 공고 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}")
    public ApiResponse<Long> removeRecruitment(
            @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        Long id = recruitmentRemoveService.remove(recruitmentId);
        return ApiResponse.success(id);
    }
}
