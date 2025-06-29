package page.clab.api.domain.members.support.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import page.clab.api.domain.members.support.application.dto.request.SupportUpdateRequestDto;
import page.clab.api.domain.members.support.application.port.in.UpdateSupportUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
@Tag(name = "Members - Support", description = "문의 사항")
public class SupportUpdateController {

    private final UpdateSupportUseCase updateSupportUseCase;

    @Operation(summary = "[U] 문의 사항 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{supportId}")
    public ApiResponse<Long> registerSupport(
            @PathVariable(name = "supportId") Long supportId,
            @Valid @RequestBody SupportUpdateRequestDto requestDto
    ) {
        Long id = updateSupportUseCase.updateSupport(supportId, requestDto);
        return ApiResponse.success(id);
    }
}
