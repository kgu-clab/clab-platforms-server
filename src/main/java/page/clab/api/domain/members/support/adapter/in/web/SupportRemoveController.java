package page.clab.api.domain.members.support.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.support.application.port.in.RemoveSupportUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
@Tag(name = "Members - Support", description = "문의 사항")
public class SupportRemoveController {

    private final RemoveSupportUseCase removeSupportUseCase;

    @Operation(summary = "[U] 문의 사항 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{supportId}")
    public ApiResponse<Long> removeSupport(
        @PathVariable(name = "supportId") Long supportId
    ) {
        Long id = removeSupportUseCase.removeSupport(supportId);
        return ApiResponse.success(id);
    }
}
