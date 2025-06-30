package page.clab.api.domain.members.support.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.support.application.dto.request.SupportAnswerRequestDto;
import page.clab.api.domain.members.support.application.port.in.RegisterSupportAnswerUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
@Tag(name = "Members - Support", description = "문의 사항")
public class SupportAnswerRegisterController {

    private final RegisterSupportAnswerUseCase registerSupportAnswerUseCase;

    @Operation(summary = "[U] 문의 사항 답변 생성", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{supportId}/answer")
    public ApiResponse<Long> registerSupport(
        @PathVariable(name = "supportId") Long supportId,
        @Valid @RequestBody SupportAnswerRequestDto requestDto
    ) {
        Long id = registerSupportAnswerUseCase.registerAnswer(supportId,requestDto);
        return ApiResponse.success(id);
    }
}
