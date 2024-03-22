package page.clab.api.domain.blacklistIp.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import page.clab.api.domain.blacklistIp.application.BlacklistIpService;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/blacklists")
@RequiredArgsConstructor
@Tag(name = "Blacklist IP", description = "블랙리스트 IP")
@Slf4j
public class BlacklistIpController {

    private final BlacklistIpService blacklistIpService;

    @Operation(summary = "[S] 블랙리스트 IP 추가", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel addBlacklistedIp(
            HttpServletRequest request,
            @Valid @RequestBody BlacklistIpRequestDto requestDto
    ) {
        String addedIp = blacklistIpService.addBlacklistedIp(request, requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(addedIp);
        return responseModel;
    }

    @Operation(summary = "[A] 블랙리스트 IP 목록 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getBlacklistedIps(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BlacklistIp> blacklistedIps = blacklistIpService.getBlacklistedIps(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(blacklistedIps);
        return responseModel;
    }

    @Operation(summary = "[S] 블랙리스트 IP 제거", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("")
    public ResponseModel removeBlacklistedIp(
            HttpServletRequest request,
            @RequestParam(name = "ipAddress") String ipAddress
    ) {
        String deletedIp = blacklistIpService.deleteBlacklistedIp(request, ipAddress);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(deletedIp);
        return responseModel;
    }

    @Operation(summary = "[S] 블랙리스트 IP 초기화", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/clear")
    public ResponseModel clearBlacklist(
            HttpServletRequest request
    ) {
        blacklistIpService.clearBlacklist(request);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
