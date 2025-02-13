package page.clab.api.domain.auth.blacklistIp.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.auth.blacklistIp.application.dto.response.BlacklistIpResponseDto;
import page.clab.api.domain.auth.blacklistIp.application.port.in.RetrieveBlacklistIpsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/blacklists")
@RequiredArgsConstructor
@Tag(name = "Authentication - Blacklist IP", description = "블랙리스트 IP")
public class BlacklistIpRetrievalController {

    private final RetrieveBlacklistIpsUseCase retrieveBlacklistIpsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[A] 블랙리스트 IP 목록 조회", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BlacklistIpResponseDto>> retrieveBlacklistIps(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection,
            BlacklistIpResponseDto.class);
        PagedResponseDto<BlacklistIpResponseDto> blacklistedIps = retrieveBlacklistIpsUseCase.retrieveBlacklistIps(
            pageable);
        return ApiResponse.success(blacklistedIps);
    }
}
