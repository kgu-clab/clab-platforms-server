package page.clab.api.domain.login.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.port.in.RetrieveBannedMembersUseCase;
import page.clab.api.domain.login.dto.response.AccountLockInfoResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/account-lock-info")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인")
public class BanMembersRetrievalController {

    private final RetrieveBannedMembersUseCase retrieveBannedMembersUseCase;

    @Operation(summary = "[S] 밴 멤버 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<AccountLockInfoResponseDto>> retrieveBanMembers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<AccountLockInfoResponseDto> banMembers = retrieveBannedMembersUseCase.retrieveBanMembers(pageable);
        return ApiResponse.success(banMembers);
    }
}
