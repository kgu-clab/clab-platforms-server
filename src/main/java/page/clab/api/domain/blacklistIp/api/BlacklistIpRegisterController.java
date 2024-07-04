package page.clab.api.domain.blacklistIp.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.blacklistIp.application.port.in.RegisterBlacklistIpUseCase;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/blacklists")
@RequiredArgsConstructor
@Tag(name = "Blacklist IP", description = "블랙리스트 IP")
public class BlacklistIpRegisterController {

    private final RegisterBlacklistIpUseCase registerBlacklistIpUseCase;

    @Operation(summary = "[S] 블랙리스트 IP 추가", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<String> registerBlacklistIp(
            HttpServletRequest request,
            @Valid @RequestBody BlacklistIpRequestDto requestDto
    ) {
        String addedIp = registerBlacklistIpUseCase.registerBlacklistIp(request, requestDto);
        return ApiResponse.success(addedIp);
    }
}
