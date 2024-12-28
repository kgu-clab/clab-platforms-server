package page.clab.api.domain.hiring.application.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.application.application.port.in.ApproveApplicationUseCase;
import page.clab.api.domain.hiring.application.application.port.in.RejectApplicationUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Hiring - Application", description = "동아리 지원")
public class ApplicationApprovalController {

    private final ApproveApplicationUseCase approveApplicationUseCase;
    private final RejectApplicationUseCase rejectApplicationUseCase;

    @Operation(summary = "[S] 지원 합격 처리", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
        "지원자의 상태를 합격으로 변경")
    @PreAuthorize("hasRole('SUPER')")
    @PatchMapping("/approve/{recruitmentId}/{studentId}")
    public ApiResponse<Long> approveApplication(
        @PathVariable(name = "recruitmentId") Long recruitmentId,
        @PathVariable(name = "studentId") String studentId
    ) {
        Long recruitmentID = approveApplicationUseCase.approveApplication(recruitmentId, studentId);
        return ApiResponse.success(recruitmentID);
    }

    @Operation(summary = "[S] 지원 불합격 처리", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
        "지원자의 상태를 불합격으로 변경")
    @PreAuthorize("hasRole('SUPER')")
    @PatchMapping("/reject/{recruitmentId}/{studentId}")
    public ApiResponse<Long> rejectApplication(
        @PathVariable(name = "recruitmentId") Long recruitmentId,
        @PathVariable(name = "studentId") String studentId
    ) {
        Long recruitmentID = rejectApplicationUseCase.rejectApplication(recruitmentId, studentId);
        return ApiResponse.success(recruitmentID);
    }
}
