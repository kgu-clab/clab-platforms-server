package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.BlacklistService;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.entity.BlacklistIp;

import java.util.List;

@RestController
@RequestMapping("/blacklists")
@RequiredArgsConstructor
@Tag(name = "Blacklist", description = "블랙리스트 관련 API")
@Slf4j
public class BlacklistController {

    private final BlacklistService blacklistService;

    @Operation(summary = "[A] 블랙리스트 IP 추가", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel addBlacklistedIp(
            @RequestParam String ipAddress
    ) throws PermissionDeniedException {
        blacklistService.addBlacklistedIp(ipAddress);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 블랙리스트 IP 목록 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getBlacklistedIps() throws PermissionDeniedException {
        List<BlacklistIp> blacklistedIps = blacklistService.getBlacklistedIps();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(blacklistedIps);
        return responseModel;
    }

    @Operation(summary = "[A] 블랙리스트 IP 제거", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("")
    public ResponseModel removeBlacklistedIp(
            @RequestParam String ipAddress
    ) throws PermissionDeniedException {
        blacklistService.deleteBlacklistedIp(ipAddress);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 블랙리스트 IP 초기화", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/clear")
    public ResponseModel clearBlacklist() throws PermissionDeniedException {
        blacklistService.clearBlacklist();
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
