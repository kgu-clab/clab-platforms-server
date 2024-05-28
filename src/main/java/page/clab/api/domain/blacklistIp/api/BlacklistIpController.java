package page.clab.api.domain.blacklistIp.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.blacklistIp.application.BlacklistIpService;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/blacklists")
@RequiredArgsConstructor
@Tag(name = "Blacklist IP", description = "블랙리스트 IP")
@Slf4j
public class BlacklistIpController {

    private final BlacklistIpService blacklistIpService;

    @Operation(summary = "[S] 블랙리스트 IP 추가", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<String> addBlacklistedIp(
            HttpServletRequest request,
            @Valid @RequestBody BlacklistIpRequestDto requestDto
    ) {
        String addedIp = blacklistIpService.addBlacklistedIp(request, requestDto);
        return ApiResponse.success(addedIp);
    }

    @Operation(summary = "[A] 블랙리스트 IP 목록 조회", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, ipAddress")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<BlacklistIp>> getBlacklistedIps(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, BlacklistIp.class);
        PagedResponseDto<BlacklistIp> blacklistedIps = blacklistIpService.getBlacklistedIps(pageable);
        return ApiResponse.success(blacklistedIps);
    }

    @Operation(summary = "[S] 블랙리스트 IP 제거", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("")
    public ApiResponse<String> removeBlacklistedIp(
            HttpServletRequest request,
            @RequestParam(name = "ipAddress") String ipAddress
    ) {
        String deletedIp = blacklistIpService.deleteBlacklistedIp(request, ipAddress);
        return ApiResponse.success(deletedIp);
    }

    @Operation(summary = "[S] 블랙리스트 IP 초기화", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/clear")
    public ApiResponse<List<String>> clearBlacklist(
            HttpServletRequest request
    ) {
        List<String> blacklistIps = blacklistIpService.clearBlacklist(request);
        return ApiResponse.success(blacklistIps);
    }

}
