package page.clab.api.domain.members.support.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.support.application.dto.request.SupportAnswerUpdateRequestDto;
import page.clab.api.domain.members.support.application.port.in.UpdateSupportAnswerUseCase;
import page.clab.api.global.common.dto.ApiResponse;
@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
@Tag(name = "Members - Support", description = "문의 사항")
public class SupportAnswerUpdateController {

    private final UpdateSupportAnswerUseCase updateSupportAnswerUseCase;

    @Operation(summary = "[A] 문의 사항 답변 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{supportId}/answer")
    public ApiResponse<Long> registerSupport(
        @PathVariable(name = "supportId") Long supportId,
        @Valid @RequestBody SupportAnswerUpdateRequestDto requestDto
    ) {
        Long id = updateSupportAnswerUseCase.updateAnswer(supportId, requestDto);
        return ApiResponse.success(id);
    }
}
