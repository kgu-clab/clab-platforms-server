package page.clab.api.domain.memberManagement.executive.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveUpdateRequestDto;
import page.clab.api.domain.memberManagement.executive.application.port.in.UpdateExecutiveUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/executive")
@RequiredArgsConstructor
@Tag(name = "Member Management - Executive", description = "운영진")
public class ExecutiveUpdateController {

    private final UpdateExecutiveUseCase updateExecutiveUseCase;

    @Operation(summary = "[A] 운영진 정보 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{executiveId}")
    public ApiResponse<String> updateExecutive(
        @PathVariable(name = "executiveId") String executiveId,
        @RequestBody ExecutiveUpdateRequestDto requestDto
    ) {
        String id = updateExecutiveUseCase.updateExecutive(executiveId, requestDto);
        return ApiResponse.success(id);
    }
}
