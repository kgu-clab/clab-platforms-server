package page.clab.api.domain.sharedAccount.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.sharedAccount.application.SharedAccountService;
import page.clab.api.domain.sharedAccount.application.SharedAccountUsageService;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountRequestDto;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUpdateRequestDto;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUsageRequestDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountResponseDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountUsageResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.CustomOptimisticLockingFailureException;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/shared-accounts")
@RequiredArgsConstructor
@Tag(name = "SharedAccount", description = "공동 계정")
@Slf4j
public class SharedAccountController {

    private final SharedAccountService sharedAccountService;

    private final SharedAccountUsageService sharedAccountUsageService;

    @Operation(summary = "[S] 공동 계정 추가", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createSharedAccount(
            @Valid @RequestBody SharedAccountRequestDto sharedAccountRequestDto
    ) {
        Long id = sharedAccountService.createSharedAccount(sharedAccountRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 공동 계정 조회(상태 포함)", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getSharedAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<SharedAccountResponseDto> sharedAccounts = sharedAccountService.getSharedAccounts(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(sharedAccounts);
        return responseModel;
    }

    @Operation(summary = "[S] 공동 계정 수정", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{accountId}")
    public ResponseModel updateSharedAccount(
            @PathVariable(name = "accountId") Long accountId,
            @Valid @RequestBody SharedAccountUpdateRequestDto sharedAccountUpdateRequestDto
    ) {
        Long id = sharedAccountService.updateSharedAccount(accountId, sharedAccountUpdateRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[S] 공동 계정 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{accountId}")
    public ResponseModel deleteSharedAccount(
            @PathVariable(name = "accountId") Long accountId
    ) {
        Long id = sharedAccountService.deleteSharedAccount(accountId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 공동 계정 이용 신청", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "이미 사용 또는 예약으로 등록된 시간대는 신청 불가능<br>" +
            "신청시에 계정 상태가 자동으로 바뀌므로 상태 변경은 별도로 안해도 됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/usage")
    public ResponseModel requestSharedAccountUsage(
            @Valid @RequestBody SharedAccountUsageRequestDto sharedAccountUsageRequestDto
    ) throws CustomOptimisticLockingFailureException {
        Long id = sharedAccountUsageService.requestSharedAccountUsage(sharedAccountUsageRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 공동 계정 이용 내역 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/usage")
    public ResponseModel getSharedAccountUsages(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<SharedAccountUsageResponseDto> sharedAccountUsages = sharedAccountUsageService.getSharedAccountUsages(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(sharedAccountUsages);
        return responseModel;
    }

    @Operation(summary = "[U] 공동 계정 이용 상태 변경", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "이용 중 취소/완료, 예약 취소만 가능")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/usage/{usageId}")
    public ResponseModel updateSharedAccountUsage(
            @PathVariable(name = "usageId") Long usageId,
            @RequestParam(name = "status") SharedAccountUsageStatus status
    ) throws PermissionDeniedException {
        Long id = sharedAccountUsageService.updateSharedAccountUsage(usageId, status);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}

