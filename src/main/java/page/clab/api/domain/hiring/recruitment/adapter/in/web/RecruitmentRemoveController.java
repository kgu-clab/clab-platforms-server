package page.clab.api.domain.hiring.recruitment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.recruitment.application.port.in.RemoveRecruitmentUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Hiring - Recruitment", description = "모집 공고")
public class RecruitmentRemoveController {

    private final RemoveRecruitmentUseCase removeRecruitmentUseCase;

    @Operation(summary = "[S] 모집 공고 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @DeleteMapping("/{recruitmentId}")
    public ApiResponse<Long> removeBoard(
        @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        Long id = removeRecruitmentUseCase.removeRecruitment(recruitmentId);
        return ApiResponse.success(id);
    }
}
